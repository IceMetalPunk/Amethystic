package com.IceMetalPunk.amethystic.blocks;

import java.util.Random;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEnderFlame extends BlockFire {
	public BlockEnderFlame() {
		super();
		this.setUnlocalizedName("ender_flame").setRegistryName(Amethystic.MODID, "ender_flame");
		this.setLightLevel(1.0f);
	}

	@Override
	public int tickRate(World worldIn) {
		return 10;
	}

	@Override
	public MapColor getMapColor(IBlockState state) {
		return MapColor.CYAN;
	}

	@Override // So the Ender Flame doesn't spread
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		Block block = world.getBlockState(pos.down()).getBlock();
		int i = ((Integer) state.getValue(AGE)).intValue();
		boolean flag = block.isFireSource(world, pos.down(), EnumFacing.UP);

		if (!flag && world.isRaining() && this.canDie(world, pos) && rand.nextFloat() < 0.2F + (float) i * 0.03F) {
			world.setBlockToAir(pos);
		}
		else if (i < 15) {
			state = state.withProperty(AGE, Integer.valueOf(i + 1));
			world.setBlockState(pos, state, 4);
			world.scheduleUpdate(pos, this, this.tickRate(world) + rand.nextInt(10));
		}
		else {
			world.setBlockToAir(pos);
		}
	}

	protected void teleport(EntityPlayerMP player, BlockPos destination) {
		teleport(player, destination.getX(), destination.getY(), destination.getZ());
	}

	protected void teleport(EntityPlayerMP player, int x, int y, int z) {
		player.connection.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
	}

	// Teleport the player when they walk through the flames with a linked
	// portkey
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (entity instanceof EntityPlayerMP && !entity.worldObj.isRemote) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			ItemStack mainItem = player.getHeldItemMainhand();
			ItemStack offItem = player.getHeldItemOffhand();

			NBTTagCompound tag = null;
			if (mainItem != null && mainItem.getItem() == Amethystic.items.PORTKEY && mainItem.hasTagCompound()) {
				tag = mainItem.getTagCompound();
				mainItem.damageItem(1, player);
			}
			else if (offItem != null && offItem.getItem() == Amethystic.items.PORTKEY && offItem.hasTagCompound()) {
				tag = offItem.getTagCompound();
				offItem.damageItem(1, player);
			}

			if (tag != null) {
				int x = tag.getInteger("linkX"), y = tag.getInteger("linkY"), z = tag.getInteger("linkZ");
				player.velocityChanged = true;
				teleport(player, x, y, z);
				player.setFire(1);
			}
		}
	}
}
