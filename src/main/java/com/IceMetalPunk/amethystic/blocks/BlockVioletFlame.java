package com.IceMetalPunk.amethystic.blocks;

import java.util.Random;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.BlockFire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockVioletFlame extends BlockFire {
	public BlockVioletFlame() {
		super();
		this.setUnlocalizedName("violet_flame").setRegistryName(Amethystic.MODID, "violet_flame");
		this.setLightLevel(1.0f);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
		world.setBlockState(pos, state.withProperty(AGE, 0)); // Reset age to
																// avoid dying
																// naturally.
	}

	@Override
	public MapColor getMapColor(IBlockState state) {
		return MapColor.MAGENTA;
	}
}
