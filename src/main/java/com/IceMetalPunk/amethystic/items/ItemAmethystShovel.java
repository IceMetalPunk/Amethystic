package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemSpade;

public class ItemAmethystShovel extends ItemSpade {

	protected ItemAmethystShovel() {
		super(Amethystic.AMETHYST_MATERIAL);
		this.setUnlocalizedName("amethyst_shovel").setRegistryName(Amethystic.MODID, "amethyst_shovel");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

}
