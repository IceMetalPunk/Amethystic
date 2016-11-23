package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemPickaxe;

public class ItemAmethystPickaxe extends ItemPickaxe {

	protected ItemAmethystPickaxe() {
		super(Amethystic.AMETHYST_MATERIAL);
		this.setUnlocalizedName("amethyst_pickaxe").setRegistryName(Amethystic.MODID, "amethyst_pickaxe");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

}
