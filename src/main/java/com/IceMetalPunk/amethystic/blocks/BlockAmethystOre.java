package com.IceMetalPunk.amethystic.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockAmethystOre extends BlockOre {
	public BlockAmethystOre() {
		this(Material.ROCK.getMaterialMapColor());
	}

	public BlockAmethystOre(MapColor color) {
		super(color);
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
		this.setRegistryName(Amethystic.MODID, "amethyst_ore").setUnlocalizedName("amethyst_ore");
		this.setHardness(15.0F).setResistance(5.0F);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 3);
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Amethystic.instance.items.AMETHYST;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random random) {
		return 1;
	}

	/**
	 * Get the quantity dropped based on the given fortune level
	 */
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if (fortune > 0) {
			int fortuneAmount = Math.max(0, random.nextInt(fortune + 2) - 1);
			return this.quantityDropped(random) * (fortuneAmount + 1);
		}
		else {
			return this.quantityDropped(random);
		}
	}

	@Override
	public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();
		return MathHelper.getRandomIntegerInRange(rand, 2, 5);
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called
	 * when the block gets destroyed. It returns the metadata of the dropped
	 * item based on the old metadata of the block.
	 */
	public int damageDropped(IBlockState state) {
		return 0;
	}
}
