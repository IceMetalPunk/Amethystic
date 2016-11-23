package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemBlock;

public class ItemBlockPlacedSpawner extends ItemBlock {

	public ItemBlockPlacedSpawner() {
		super(Amethystic.blocks.PLACED_SPAWNER);
		this.setUnlocalizedName("placed_Spawner").setRegistryName(Amethystic.MODID, "placed_spawner");
	}

}
