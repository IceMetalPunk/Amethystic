package com.IceMetalPunk.amethystic.AmethysticItems;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemBlock;

public class ItemBlockEverlastingFarmland extends ItemBlock {

	public ItemBlockEverlastingFarmland() {
		super(Amethystic.blocks.EVERLASTING_FARMLAND);
		this.setUnlocalizedName("everlasting_farmland").setRegistryName(Amethystic.MODID, "everlasting_farmland");
	}

}
