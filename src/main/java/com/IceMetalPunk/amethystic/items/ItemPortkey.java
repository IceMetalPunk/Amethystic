package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemPortkey extends Item {
	public ItemPortkey() {
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
		this.setUnlocalizedName("portkey").setRegistryName(Amethystic.MODID, "portkey");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

	// Link portkey to current position when used
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (!worldIn.isRemote) {
			// return new ActionResult(EnumActionResult.SUCCESS, stack);
		}
		stack = playerIn.getHeldItem(hand);
		NBTTagCompound current = new NBTTagCompound();
		if (stack.hasTagCompound()) {
			current = stack.getTagCompound();
		}
		BlockPos pos = playerIn.getPosition();
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		current.setInteger("linkX", x);
		current.setInteger("linkY", y);
		current.setInteger("linkZ", z);
		playerIn.addChatComponentMessage(new TextComponentString("Portkey linked to (" + x + ", " + y + ", " + z + ")"));
		stack.setTagCompound(current);
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
}
