package com.IceMetalPunk.amethystic.tileEntities;

import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPlacedSpawner extends TileEntityMobSpawner {

	// Copy the spawner logic instantiation using our custom class.
	private final PlacedSpawnerBaseLogic spawnerLogic = new PlacedSpawnerBaseLogic() {
		public void broadcastEvent(int id) {
			TileEntityPlacedSpawner.this.worldObj.addBlockEvent(TileEntityPlacedSpawner.this.pos, Blocks.MOB_SPAWNER,
					id, 0);
		}

		public World getSpawnerWorld() {
			return TileEntityPlacedSpawner.this.worldObj;
		}

		public BlockPos getSpawnerPosition() {
			return TileEntityPlacedSpawner.this.pos;
		}

		public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
			super.setNextSpawnData(p_184993_1_);

			if (this.getSpawnerWorld() != null) {
				IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
				this.getSpawnerWorld().notifyBlockUpdate(TileEntityPlacedSpawner.this.pos, iblockstate, iblockstate, 4);
			}
		}
	};

	// Now custom data
	UUID playerLinked = null;

	public TileEntityPlacedSpawner() {
		this(null);
	}

	public TileEntityPlacedSpawner(UUID player) {
		super();
		this.playerLinked = player;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (this.playerLinked == null) {
			tag.removeTag("linkedPlayer");
		} else {
			tag.setUniqueId("linkedPlayer", this.playerLinked);
		}
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.playerLinked = tag.getUniqueId("linkedPlayer");
		this.spawnerLogic.setLinkedPlayer(this.playerLinked);
	}
}