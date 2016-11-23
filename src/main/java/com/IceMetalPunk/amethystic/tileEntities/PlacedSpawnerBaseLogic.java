package com.IceMetalPunk.amethystic.tileEntities;

import java.lang.reflect.Field;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public abstract class PlacedSpawnerBaseLogic extends MobSpawnerBaseLogic {
	protected UUID playerLinked = null;
	protected Field rangeField = null, delayField = null;

	// Constructor set up reflection needed because things are private...
	public PlacedSpawnerBaseLogic() {
		super();
		try {
			rangeField = MobSpawnerBaseLogic.class.getDeclaredField("activatingRangeFromPlayer");
			rangeField.setAccessible(true);
			delayField = MobSpawnerBaseLogic.class.getDeclaredField("spawnDelay");
			delayField.setAccessible(true);

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	// Setters and getters for linking players to spawners
	public void setLinkedPlayer(UUID player) {
		this.playerLinked = player;
	}

	@Nullable
	public UUID getLinkedPlayer(UUID player) {
		return this.playerLinked;
	}

	// Only active if the linked player is on the server
	private boolean isActivated() {
		EntityPlayer player = this.getSpawnerWorld().getPlayerEntityByUUID(this.playerLinked);
		BlockPos blockpos = this.getSpawnerPosition();
		try {
			return (player != null && this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double) blockpos.getX() + 0.5D,
					(double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D, this.rangeField.getDouble(this)));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Update spawner
	@Override
	public void updateSpawner() {
		if (isActivated()) {
			EntityPlayer player = this.getSpawnerWorld().getPlayerEntityByUUID(this.playerLinked);
			if (player == null) {
				return;
			} else {
				try {
					// Only damage the player when the spawner would spawn, not
					// every update tick
					if (this.delayField.getInt(this) <= 1) {
						player.attackEntityFrom(DamageSource.magic, 2.0f);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			super.updateSpawner();
		}
	}
}
