package com.IceMetalPunk.amethystic.blocks;

import com.IceMetalPunk.amethystic.Amethystic;
import com.IceMetalPunk.amethystic.tileEntities.TileEntityPlacedSpawner;

import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPlacedSpawner extends BlockMobSpawner {

	public BlockPlacedSpawner() {
		super();
		this.setHardness(5.0F).setResistance(10.0F);
		this.setSoundType(SoundType.STONE);
		this.setUnlocalizedName("placed_spawner").setRegistryName(Amethystic.MODID, "placed_spawner")
				.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPlacedSpawner();
	}
}
