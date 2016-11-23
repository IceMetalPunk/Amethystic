package com.IceMetalPunk.amethystic.blocks;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAmethyst extends Block {

	public BlockAmethyst() {
		super(Material.IRON, MapColor.MAGENTA);
		this.setHardness(5.0F).setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName("amethyst_block").setRegistryName(Amethystic.MODID, "amethyst_block")
				.setCreativeTab(Amethystic.AMETHYSTIC_TAB);

	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}
}
