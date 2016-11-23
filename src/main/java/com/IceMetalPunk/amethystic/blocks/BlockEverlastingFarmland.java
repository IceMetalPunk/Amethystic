package com.IceMetalPunk.amethystic.blocks;

import java.util.Random;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockEverlastingFarmland extends BlockFarmland {
	public BlockEverlastingFarmland() {
		super();
		this.setHardness(0.6F).setUnlocalizedName("everlasting_farmland");
		this.setSoundType(SoundType.GROUND);
		this.setRegistryName(Amethystic.MODID, "everlasting_farmland");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		worldIn.setBlockState(pos, state.withProperty(MOISTURE, 7));
	}

	// Override onFallenUpon, passing a 0 fall distance so it acts like nothing
	// fell on it, thus not converting to dirt.
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		super.onFallenUpon(worldIn, pos, entityIn, 0);
	}

	// Allow crop planting on it
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			IPlantable plantable) {
		EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
		return plantType == EnumPlantType.Crop || plantType == EnumPlantType.Plains;
	}
}
