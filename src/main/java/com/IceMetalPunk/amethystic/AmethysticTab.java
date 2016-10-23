package com.IceMetalPunk.amethystic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AmethysticTab extends CreativeTabs {

	public AmethysticTab() {
		super(CreativeTabs.CREATIVE_TAB_ARRAY.length, "amethystic");
	}

	@Override
	public Item getTabIconItem() {
		return Amethystic.items.AMETHYST;
	}

}
