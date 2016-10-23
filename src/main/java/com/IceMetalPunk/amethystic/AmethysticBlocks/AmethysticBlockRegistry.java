package com.IceMetalPunk.amethystic.AmethysticBlocks;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class AmethysticBlockRegistry {
	public static Block AMETHYST_ORE = new BlockAmethystOre();
	public static Block AMETHYST_BLOCK = new BlockAmethyst();
	public static Block VIOLET_FLAME = new BlockVioletFlame();
	public static Block ENDER_FLAME = new BlockEnderFlame();
	public static Block EVERLASTING_FARMLAND = new BlockEverlastingFarmland();

	// Ores
	public static HashMap<String, Block> oreList = new HashMap<String, Block>();

	public AmethysticBlockRegistry() {
		GameRegistry.register(AMETHYST_ORE);
		oreList.put("oreAmethyst", AMETHYST_ORE);
		GameRegistry.register(AMETHYST_BLOCK);
		GameRegistry.register(VIOLET_FLAME);
		GameRegistry.register(ENDER_FLAME);
		GameRegistry.register(EVERLASTING_FARMLAND);

		// Register ore dictionary stuff
		for (Entry e : oreList.entrySet()) {
			OreDictionary.registerOre((String) e.getKey(), (Block) e.getValue());
		}
	}
}
