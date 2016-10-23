package com.IceMetalPunk.amethystic.worldGen;

import java.util.Random;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class AmethysticWorldGenerator implements IWorldGenerator {

	private IBlockState amethystOreState = Amethystic.blocks.AMETHYST_ORE.getDefaultState();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimensionType()) {
		case NETHER:
			int maxHeight = 40, minHeight = 1;
			int heightDiff = maxHeight - minHeight + 1;
			if (random.nextInt(5) == 0) {
				int x = chunkX * 16 + random.nextInt(16);
				int y = minHeight + random.nextInt(heightDiff);
				int z = chunkZ * 16 + random.nextInt(16);
				for (int xx = x - 1; xx <= x + 1; ++xx) {
					for (int yy = Math.max(1, y - 1); yy <= Math.min(y + 1,
							world.provider.getActualHeight() - 1); ++yy) {
						for (int zz = z - 1; zz <= z + 1; ++zz) {
							BlockPos pos = new BlockPos(xx, yy, zz);
							if (world.getBlockState(pos).getBlock() == Blocks.NETHERRACK && random.nextInt(5) == 0) {
								world.setBlockState(pos, amethystOreState);
							}
						}
					}
				}
			}
			break;
		default:
			return;
		}
	}

}
