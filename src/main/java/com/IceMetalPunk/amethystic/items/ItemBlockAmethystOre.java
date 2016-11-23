package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemBlock;

public class ItemBlockAmethystOre extends ItemBlock {

	public ItemBlockAmethystOre() {
		super(Amethystic.blocks.AMETHYST_ORE);
		this.setUnlocalizedName("amethyst_ore").setRegistryName(Amethystic.MODID, "amethyst_ore");
	}

}
