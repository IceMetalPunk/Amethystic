package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlooPowder extends Item {
	public ItemFlooPowder() {
		super();
		this.setUnlocalizedName("floo_powder").setRegistryName(Amethystic.MODID, "floo_powder");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

	// Override item use just to change the type of fire it produces
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		pos = pos.offset(facing);

		if (!playerIn.canPlayerEdit(pos, facing, stack)) {
			return EnumActionResult.FAIL;
		}
		else {
			if (worldIn.isAirBlock(pos)) {
				// TODO: Floo powder whoosh sound?
				// worldIn.playSound(playerIn, pos,
				// SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS,
				// 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlockState(pos, Amethystic.blocks.ENDER_FLAME.getDefaultState(), 11);
			}

			--stack.stackSize;
			return EnumActionResult.SUCCESS;
		}
	}
}
