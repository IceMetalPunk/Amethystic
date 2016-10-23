package com.IceMetalPunk.amethystic.AmethysticItems;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBlockAmethystOre extends ItemBlock {

	public ItemBlockAmethystOre() {
		super(Amethystic.blocks.AMETHYST_ORE);
		this.setUnlocalizedName("amethyst_ore").setRegistryName(Amethystic.MODID, "amethyst_ore");
	}

}
