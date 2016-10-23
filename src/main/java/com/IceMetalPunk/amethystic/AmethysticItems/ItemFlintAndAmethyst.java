package com.IceMetalPunk.amethystic.AmethysticItems;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlintAndAmethyst extends ItemFlintAndSteel {
	public ItemFlintAndAmethyst() {
		super();
		this.setUnlocalizedName("flint_and_amethyst").setRegistryName(Amethystic.MODID, "flint_and_amethyst");
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
				worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlockState(pos, Amethystic.blocks.VIOLET_FLAME.getDefaultState(), 11);
			}

			stack.damageItem(1, playerIn);
			return EnumActionResult.SUCCESS;
		}
	}
}
