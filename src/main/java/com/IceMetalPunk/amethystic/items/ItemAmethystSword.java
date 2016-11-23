package com.IceMetalPunk.amethystic.items;

import com.IceMetalPunk.amethystic.Amethystic;

import net.minecraft.item.ItemSword;

public class ItemAmethystSword extends ItemSword {

	public final float realAttackDamage;

	protected ItemAmethystSword() {
		super(Amethystic.AMETHYST_MATERIAL);
		this.realAttackDamage = 3.0f + Amethystic.AMETHYST_MATERIAL.getDamageVsEntity();
		this.setUnlocalizedName("amethyst_sword").setRegistryName(Amethystic.MODID, "amethyst_sword");
		this.setCreativeTab(Amethystic.AMETHYSTIC_TAB);
	}

	// Make it deal more damage to Withers and Nether mobs
	/*
	 * @Override public boolean hitEntity(ItemStack stack, EntityLivingBase
	 * target, EntityLivingBase attacker) { super.hitEntity(stack, target,
	 * attacker); if (target.isImmuneToFire() && !(target instanceof
	 * EntityShulker) && !(target instanceof EntityDragon)) {
	 * System.out.println("There's a Nether mob getting hit!");
	 * target.hurtResistantTime = 0;
	 * 
	 * if (attacker instanceof EntityPlayer) { EntityPlayer player =
	 * (EntityPlayer) attacker; boolean success =
	 * target.attackEntityFrom(DamageSource.causePlayerDamage(player),
	 * player.getCooledAttackStrength(0.5f)); if (success) { System.out.println(
	 * "Dealt double damage!"); } else { System.out.println("Didn't do it!");
	 * 
	 * } } else { boolean success =
	 * target.attackEntityFrom(DamageSource.causeMobDamage(attacker), 1.0f +
	 * this.realAttackDamage); if (success) { System.out.println(
	 * "Dealt double damage from mob!"); } else { System.out.println(
	 * "Didn't do it from mob!"); } } } return true; }
	 */

}
