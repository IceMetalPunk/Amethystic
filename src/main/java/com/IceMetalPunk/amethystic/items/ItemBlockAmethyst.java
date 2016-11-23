package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemBlock;

public class ItemBlockAmethyst extends ItemBlock {

	public ItemBlockAmethyst() {
		super(Amethystic.blocks.AMETHYST_BLOCK);
		this.setUnlocalizedName("amethyst_block").setRegistryName(Amethystic.MODID, "amethyst_block");
	}

}
