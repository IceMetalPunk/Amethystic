/* TODO:
 * 
 * -Amethyst Beacon Base
 * --> Provides negative effects instead of positive ones
 * -Amethyst Enchanting
 * --> Using amethyst instead of lapis gives better enchantments for cheaper, but with chance of curses as wel
 * -Floo Teleportation
 * --> Portkeys store a single coordinate destination
 * --> Floo Powder creates ender-blue Ender Flames, which die in a short time
 * --> ^Walking into Ender Flames with a marked Portkey in your hand teleports you to the Portkey's destination
 * ----> It also extinguishes the flame and the portkey takes durability
 * --> Main hand takes precedence over offhand, but offhand works if no Portkey in main hand
 * --> Portkey recipe: [" AA", "EAE", " E ", 'E' => ender_eye, 'A' => amethyst]
 * --> Floo Powder recipe: ["PGP", "GAG", "PGP", 'P' => ender_pearl, 'A' => amethyst, 'G' => gunpowder] 
 * 
 * COMPLETED:
 *  - !Amethyst tools and armor that can't be enchanted - DONE!
 *  - !Amethyst Armor (each piece reduces duration of negative status effects by 1/2) - DONE!
 *  - !Flint-and-amethyst (makes purple flame that never goes out by age) - DONE!
 *  - !Amethyst hoe - Everlasting Farmland that stays hydrated without water - DONE!
 *  - !Amethyst axe (10% chance of treecapitation on logs) - DONE!
 *  - !Amethyst pick (10% chance of auto-smelting dropped items) - DONE!
 *  - !Amethyst shovel (digs 3x3x1, taking appropriate durability) - DONE!
 *  - !Amethyst sword (2x damage to Nether mobs, including wither) - DONE!
 */

package com.IceMetalPunk.amethystic;

import com.IceMetalPunk.amethystic.AmethysticBlocks.AmethysticBlockRegistry;
import com.IceMetalPunk.amethystic.AmethysticItems.AmethysticItemRegistry;
import com.IceMetalPunk.amethystic.commands.CommandBlockLight;
import com.IceMetalPunk.amethystic.commands.CommandClearTags;
import com.IceMetalPunk.amethystic.commands.CommandLight;
import com.IceMetalPunk.amethystic.commands.CommandSkyLight;
import com.IceMetalPunk.amethystic.commands.CommandTagBiome;
import com.IceMetalPunk.amethystic.commands.CommandTestForChat;
import com.IceMetalPunk.amethystic.commands.CommandTestForSky;
import com.IceMetalPunk.amethystic.events.AmethysticEventHandler;
import com.IceMetalPunk.amethystic.worldGen.AmethysticWorldGenerator;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Amethystic.MODID, version = Amethystic.VERSION)
public class Amethystic {

	// Global mod metadata
	public static final String MODID = "amethystic";
	public static final String VERSION = "1.0";

	@Instance("Mod")
	public static Amethystic instance = new Amethystic();

	// Creative tabs
	public static AmethysticTab AMETHYSTIC_TAB = new AmethysticTab();

	// Event handlers
	public static final AmethysticEventHandler eventListener = new AmethysticEventHandler(MinecraftForge.EVENT_BUS);

	// Tool materials
	public static ToolMaterial AMETHYST_MATERIAL = EnumHelper.addToolMaterial("AMETHYST", 3, 1561, 8.0f, 3.0f, 0);
	public static ArmorMaterial AMETHYST_ARMOR_MATERIAL = EnumHelper.addArmorMaterial("AMETHYST", "amethystic:amethyst", 33, new int[] {
			3, 6, 8, 3 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f);

	// Registries
	public static AmethysticItemRegistry items = new AmethysticItemRegistry();
	public static AmethysticBlockRegistry blocks = new AmethysticBlockRegistry();

	// Pre-init handling
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Register models client-side
		if (event.getSide() == Side.CLIENT) {
			items.registerModels();
		}
	}

	// Loading handlers
	@EventHandler
	public void init(FMLInitializationEvent event) {

		// Repair materials for tools
		this.AMETHYST_MATERIAL.setRepairItem(new ItemStack(this.items.AMETHYST));

		// World generation
		GameRegistry.registerWorldGenerator(new AmethysticWorldGenerator(), 1);

		// Crafting recipes
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_BLOCK), "AAA", "AAA", "AAA", 'A', items.AMETHYST);

		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_AXE), "AA", "AS", " S", 'A', items.AMETHYST, 'S', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_HOE), "AA", " S", " S", 'A', items.AMETHYST, 'S', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_PICKAXE), "AAA", " S ", " S ", 'A', items.AMETHYST, 'S', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_SHOVEL), "A", "S", "S", 'A', items.AMETHYST, 'S', Items.STICK);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_SWORD), "A", "A", "S", 'A', items.AMETHYST, 'S', Items.STICK);
		GameRegistry.addShapelessRecipe(new ItemStack(items.FLINT_AND_AMETHYST), new Object[] { Items.FLINT,
				items.AMETHYST });

		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_HELMET), "AAA", "A A", 'A', items.AMETHYST);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_LEGGINGS), "AAA", "A A", "A A", 'A', items.AMETHYST);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_CHESTPLATE), "A A", "AAA", "AAA", 'A', items.AMETHYST);
		GameRegistry.addRecipe(new ItemStack(items.AMETHYST_BOOTS), "A A", "A A", 'A', items.AMETHYST);
		GameRegistry.addRecipe(new ItemStack(items.FLOO_POWDER, 8), "PGP", "GAG", "PGP", 'A', items.AMETHYST, 'P', Items.ENDER_PEARL, 'G', Items.GUNPOWDER);
		GameRegistry.addRecipe(new ItemStack(items.PORTKEY), " A ", "AEA", " S ", 'A', items.AMETHYST, 'E', Items.ENDER_EYE, 'S', Items.STICK);

		// Smelting recipes
		GameRegistry.addSmelting(blocks.AMETHYST_ORE, new ItemStack(items.AMETHYST, 1), 2);
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent ev) {
		ev.registerServerCommand(new CommandBlockLight());
		ev.registerServerCommand(new CommandSkyLight());
		ev.registerServerCommand(new CommandLight());
		ev.registerServerCommand(new CommandTestForSky());
		ev.registerServerCommand(new CommandTagBiome());
		ev.registerServerCommand(new CommandClearTags());
		ev.registerServerCommand(new CommandTestForChat());
	}
}
