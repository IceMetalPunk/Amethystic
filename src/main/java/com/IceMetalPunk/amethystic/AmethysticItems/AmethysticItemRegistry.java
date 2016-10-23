package com.IceMetalPunk.amethystic.AmethysticItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class AmethysticItemRegistry {
	public static ArrayList<Item> itemList = new ArrayList<Item>();
	public static Item AMETHYST = new ItemAmethyst();

	public static Item AMETHYST_AXE = new ItemAmethystAxe();
	public static Item AMETHYST_HOE = new ItemAmethystHoe();
	public static Item AMETHYST_PICKAXE = new ItemAmethystPickaxe();
	public static Item AMETHYST_SWORD = new ItemAmethystSword();
	public static Item AMETHYST_SHOVEL = new ItemAmethystShovel();
	public static Item FLINT_AND_AMETHYST = new ItemFlintAndAmethyst();
	public static Item AMETHYST_BUCKET = new ItemAmethystBucket(Blocks.AIR, "amethyst_bucket");
	public static Item AMETHYST_WATER_BUCKET = new ItemAmethystBucket(Blocks.WATER, "amethyst_water_bucket");
	public static Item AMETHYST_LAVA_BUCKET = new ItemAmethystBucket(Blocks.LAVA, "amethyst_lava_bucket");

	public static Item AMETHYST_ORE = new ItemBlockAmethystOre();
	public static Item AMETHYST_BLOCK = new ItemBlockAmethyst();
	public static Item EVERLASTING_FARMLAND = new ItemBlockEverlastingFarmland();

	public static Item AMETHYST_CHESTPLATE = new ItemAmethystArmor(EntityEquipmentSlot.CHEST, "amethyst_chestplate");
	public static Item AMETHYST_HELMET = new ItemAmethystArmor(EntityEquipmentSlot.HEAD, "amethyst_helmet");
	public static Item AMETHYST_BOOTS = new ItemAmethystArmor(EntityEquipmentSlot.FEET, "amethyst_boots");
	public static Item AMETHYST_LEGGINGS = new ItemAmethystArmor(EntityEquipmentSlot.LEGS, "amethyst_leggings");

	public static Item FLOO_POWDER = new ItemFlooPowder();
	public static Item PORTKEY = new ItemPortkey();

	// Ores list
	public static HashMap<String, Item> oreList = new HashMap<String, Item>();

	public AmethysticItemRegistry() {
		GameRegistry.register(AMETHYST);
		itemList.add(AMETHYST);
		oreList.put("gemAmethyst", AMETHYST);

		GameRegistry.register(AMETHYST_AXE);
		itemList.add(AMETHYST_AXE);
		GameRegistry.register(AMETHYST_PICKAXE);
		itemList.add(AMETHYST_PICKAXE);
		GameRegistry.register(AMETHYST_HOE);
		itemList.add(AMETHYST_HOE);
		GameRegistry.register(AMETHYST_SHOVEL);
		itemList.add(AMETHYST_SHOVEL);
		GameRegistry.register(AMETHYST_SWORD);
		itemList.add(AMETHYST_SWORD);
		GameRegistry.register(FLINT_AND_AMETHYST);
		itemList.add(FLINT_AND_AMETHYST);
		GameRegistry.register(AMETHYST_BUCKET);
		itemList.add(AMETHYST_BUCKET);
		GameRegistry.register(AMETHYST_WATER_BUCKET);
		itemList.add(AMETHYST_WATER_BUCKET);
		GameRegistry.register(AMETHYST_LAVA_BUCKET);
		itemList.add(AMETHYST_LAVA_BUCKET);

		GameRegistry.register(AMETHYST_ORE);
		itemList.add(AMETHYST_ORE);
		GameRegistry.register(AMETHYST_BLOCK);
		itemList.add(AMETHYST_BLOCK);
		GameRegistry.register(EVERLASTING_FARMLAND);
		itemList.add(EVERLASTING_FARMLAND);

		GameRegistry.register(AMETHYST_CHESTPLATE);
		itemList.add(AMETHYST_CHESTPLATE);
		GameRegistry.register(AMETHYST_HELMET);
		itemList.add(AMETHYST_HELMET);
		GameRegistry.register(AMETHYST_BOOTS);
		itemList.add(AMETHYST_BOOTS);
		GameRegistry.register(AMETHYST_LEGGINGS);
		itemList.add(AMETHYST_LEGGINGS);

		GameRegistry.register(FLOO_POWDER);
		itemList.add(FLOO_POWDER);
		GameRegistry.register(PORTKEY);
		itemList.add(PORTKEY);

		// Register ores
		for (Entry e : oreList.entrySet()) {
			OreDictionary.registerOre((String) e.getKey(), (Item) e.getValue());
		}
	}

	public void registerModel(Item which, ResourceLocation registryName) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation modelLocation = new ModelResourceLocation(registryName, "inventory");
		ModelLoader.setCustomModelResourceLocation(which, 0, modelLocation);
	}

	public void registerModel(Item which, String registryName) {
		registerModel(which, Amethystic.MODID, registryName);
	}

	public void registerModel(Item which, String mod, String registryName) {
		registerModel(which, new ResourceLocation(mod, registryName));
	}

	public void registerModels() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation modelLocation = null;
		for (Item i : itemList) {
			if (i instanceof ItemBlock) {
				registerModel(i, ((ItemBlock) i).block.getRegistryName());
			}
			else {
				registerModel(i, i.getRegistryName());
			}
		}
	}
}
