package com.IceMetalPunk.amethystic.AmethysticItems;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemAxe;

public class ItemAmethystAxe extends ItemAxe {

	protected ItemAmethystAxe() {
		super(Amethystic.AMETHYST_MATERIAL, 8.0f, -3.0f);
		this.setUnlocalizedName("amethyst_axe").setRegistryName(Amethystic.MODID, "amethyst_axe");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

}
