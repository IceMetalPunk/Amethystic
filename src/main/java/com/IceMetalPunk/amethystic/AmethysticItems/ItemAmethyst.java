package com.IceMetalPunk.amethystic.AmethysticItems;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.Item;

public class ItemAmethyst extends Item {
	public ItemAmethyst() {
		this.setUnlocalizedName("amethyst").setRegistryName(Amethystic.MODID, "amethyst")
				.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}
}
