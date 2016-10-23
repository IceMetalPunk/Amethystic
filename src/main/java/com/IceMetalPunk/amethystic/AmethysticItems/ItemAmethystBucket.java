package com.IceMetalPunk.amethystic.AmethysticItems;

import javax.annotation.Nullable;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

// Because isFull is, sadly, private, rather than using reflection to fix that, I'm just recreating the entire ItemBucket class
public class ItemAmethystBucket extends Item {
	/** field for checking if the bucket has been filled. */
	protected final Block isFull;

	public ItemAmethystBucket(Block containedBlock, String resourceLocation) {
		this.maxStackSize = 1;
		this.isFull = containedBlock;
		this.setUnlocalizedName(resourceLocation).setRegistryName(Amethystic.MODID, resourceLocation).setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		boolean flag = this.isFull == Blocks.AIR;
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		else {
			BlockPos blockpos = raytraceresult.getBlockPos();

			if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
				return new ActionResult(EnumActionResult.FAIL, itemStackIn);
			}
			else if (flag) {
				if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemStackIn)) {
					return new ActionResult(EnumActionResult.FAIL, itemStackIn);
				}
				else {
					IBlockState iblockstate = worldIn.getBlockState(blockpos);
					Material material = iblockstate.getMaterial();

					if (material == Material.WATER && ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
						return new ActionResult(EnumActionResult.SUCCESS, this.fillBucket(itemStackIn, playerIn, Amethystic.items.AMETHYST_WATER_BUCKET));
					}
					else if (material == Material.LAVA && ((Integer) iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
						playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
						worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
						playerIn.addStat(StatList.getObjectUseStats(this));
						return new ActionResult(EnumActionResult.SUCCESS, this.fillBucket(itemStackIn, playerIn, Amethystic.items.AMETHYST_LAVA_BUCKET));
					}
					else {
						return new ActionResult(EnumActionResult.FAIL, itemStackIn);
					}
				}
			}
			else {
				boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP	? blockpos
																						: blockpos.offset(raytraceresult.sideHit);

				if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemStackIn)) {
					return new ActionResult(EnumActionResult.FAIL, itemStackIn);
				}
				else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1)) {
					playerIn.addStat(StatList.getObjectUseStats(this));
					return !playerIn.capabilities.isCreativeMode	? new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Amethystic.items.AMETHYST_BUCKET))
																	: new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
				}
				else {
					return new ActionResult(EnumActionResult.FAIL, itemStackIn);
				}
			}
		}
	}

	private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {
		if (player.capabilities.isCreativeMode) {
			return emptyBuckets;
		}
		else if (--emptyBuckets.stackSize <= 0) {
			return new ItemStack(fullBucket);
		}
		else {
			if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
				player.dropItem(new ItemStack(fullBucket), false);
			}

			return emptyBuckets;
		}
	}

	// The only real change I needed: removing the check for vaporizing water.
	public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer worldIn, World pos, BlockPos posIn) {
		if (this.isFull == Blocks.AIR) {
			return false;
		}
		else {
			IBlockState iblockstate = pos.getBlockState(posIn);
			Material material = iblockstate.getMaterial();
			boolean flag = !material.isSolid();
			boolean flag1 = iblockstate.getBlock().isReplaceable(pos, posIn);

			if (!pos.isAirBlock(posIn) && !flag && !flag1) {
				return false;
			}
			else {
				if (!pos.isRemote && (flag || flag1) && !material.isLiquid()) {
					pos.destroyBlock(posIn, true);
				}

				SoundEvent soundevent = this.isFull == Blocks.FLOWING_LAVA	? SoundEvents.ITEM_BUCKET_EMPTY_LAVA
																			: SoundEvents.ITEM_BUCKET_EMPTY;
				pos.playSound(worldIn, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
				IBlockState state = this.isFull.getDefaultState();

				// FIXME: Placed water/lava don't automatically update and so
				// don't flow until they get an update.
				pos.setBlockState(posIn, state, 11);
				pos.notifyNeighborsRespectDebug(posIn, this.isFull);
				return true;
			}
		}
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack,
			net.minecraft.nbt.NBTTagCompound nbt) {
		return super.initCapabilities(stack, nbt);
	}
}