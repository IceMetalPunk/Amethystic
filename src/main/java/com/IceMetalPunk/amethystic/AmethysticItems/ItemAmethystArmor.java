package com.IceMetalPunk.amethystic.AmethysticItems;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class ItemAmethystArmor extends ItemArmor {

	public ItemAmethystArmor(EntityEquipmentSlot equipmentSlotIn, String name) {
		super(Amethystic.AMETHYST_ARMOR_MATERIAL, 5, equipmentSlotIn);
		this.setUnlocalizedName(name).setRegistryName(name).setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

}
