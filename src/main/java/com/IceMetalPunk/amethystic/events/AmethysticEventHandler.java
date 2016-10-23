package com.IceMetalPunk.amethystic.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.annotation.Nullable;

import com.IceMetalPunk.amethystic.Amethystic;
import com.IceMetalPunk.amethystic.AmethysticItems.ItemAmethystArmor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class AmethysticEventHandler {
	Method deincPotion = null;
	HashMap<UUID, EnumFacing> facingMap = new HashMap<UUID, EnumFacing>();

	public AmethysticEventHandler(EventBus bus) {
		bus.register(this);

		// Hacky way of being able to deincrement potion durations despite the
		// private nonsense
		try {
			deincPotion = PotionEffect.class.getDeclaredMethod("deincrementDuration");
			deincPotion.setAccessible(true);
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	// Modification of default attack code that allows custom damage multipliers
	// to be used
	public void attackWithCurrentItemExt(EntityPlayer player, Entity targetEntity, float multiplier) {
		if (targetEntity.canBeAttackedWithItem()) {
			if (!targetEntity.hitByEntity(player)) {
				float f = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				float f1;

				if (targetEntity instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), ((EntityLivingBase) targetEntity).getCreatureAttribute());
				}
				else {
					f1 = EnchantmentHelper.getModifierForCreature(player.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
				}

				float f2 = player.getCooledAttackStrength(0.5F);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				player.resetCooldown();

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackModifier(player);

					if (player.isSprinting() && flag) {
						player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}

					boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding() && targetEntity instanceof EntityLivingBase;
					flag2 = flag2 && !player.isSprinting();

					if (flag2) {
						f *= 1.5F;
					}

					f = f + f1;
					boolean flag3 = false;
					double d0 = (double) (player.distanceWalkedModified - player.prevDistanceWalkedModified);

					if (flag && !flag2 && !flag1 && player.onGround && d0 < (double) player.getAIMoveSpeed()) {
						ItemStack itemstack = player.getHeldItem(EnumHand.MAIN_HAND);

						if (itemstack != null && itemstack.getItem() instanceof ItemSword) {
							flag3 = true;
						}
					}

					float f4 = 0.0F;
					boolean flag4 = false;
					int j = EnchantmentHelper.getFireAspectModifier(player);

					if (targetEntity instanceof EntityLivingBase) {
						f4 = ((EntityLivingBase) targetEntity).getHealth();

						if (j > 0 && !targetEntity.isBurning()) {
							flag4 = true;
							targetEntity.setFire(1);
						}
					}

					double d1 = targetEntity.motionX;
					double d2 = targetEntity.motionY;
					double d3 = targetEntity.motionZ;
					boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), f * multiplier);

					if (flag5) {
						if (i > 0) {
							if (targetEntity instanceof EntityLivingBase) {
								((EntityLivingBase) targetEntity).knockBack(player, (float) i * 0.5F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
							}
							else {
								targetEntity.addVelocity((double) (-MathHelper.sin(player.rotationYaw * 0.017453292F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(player.rotationYaw * 0.017453292F) * (float) i * 0.5F));
							}

							player.motionX *= 0.6D;
							player.motionZ *= 0.6D;
							player.setSprinting(false);
						}

						if (flag3) {
							for (EntityLivingBase entitylivingbase : player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().expand(1.0D, 0.25D, 1.0D))) {
								if (entitylivingbase != player && entitylivingbase != targetEntity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSqToEntity(entitylivingbase) < 9.0D) {
									entitylivingbase.knockBack(player, 0.4F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(player.rotationYaw * 0.017453292F)));
									entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F);
								}
							}

							player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
							player.spawnSweepParticles();
						}

						if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
							((EntityPlayerMP) targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
							targetEntity.velocityChanged = false;
							targetEntity.motionX = d1;
							targetEntity.motionY = d2;
							targetEntity.motionZ = d3;
						}

						if (flag2) {
							player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
							player.onCriticalHit(targetEntity);
						}

						if (!flag2 && !flag3) {
							if (flag) {
								player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
							}
							else {
								player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1.0F, 1.0F);
							}
						}

						if (f1 > 0.0F) {
							player.onEnchantmentCritical(targetEntity);
						}

						if (!player.worldObj.isRemote && targetEntity instanceof EntityPlayer) {
							EntityPlayer entityplayer = (EntityPlayer) targetEntity;
							ItemStack itemstack2 = player.getHeldItemMainhand();
							ItemStack itemstack3 = entityplayer.isHandActive()	? entityplayer.getActiveItemStack()
																				: null;

							if (itemstack2 != null && itemstack3 != null && itemstack2.getItem() instanceof ItemAxe && itemstack3.getItem() == Items.SHIELD) {
								float f3 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(player) * 0.05F;

								if (flag1) {
									f3 += 0.75F;
								}

								if (player.getRNG().nextFloat() < f3) {
									entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
									player.worldObj.setEntityState(entityplayer, (byte) 30);
								}
							}
						}

						if (f >= 18.0F) {
							player.addStat(AchievementList.OVERKILL);
						}

						player.setLastAttacker(targetEntity);

						if (targetEntity instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments((EntityLivingBase) targetEntity, player);
						}

						EnchantmentHelper.applyArthropodEnchantments(player, targetEntity);
						ItemStack itemstack1 = player.getHeldItemMainhand();
						Entity entity = targetEntity;

						if (targetEntity instanceof EntityDragonPart) {
							IEntityMultiPart ientitymultipart = ((EntityDragonPart) targetEntity).entityDragonObj;

							if (ientitymultipart instanceof EntityLivingBase) {
								entity = (EntityLivingBase) ientitymultipart;
							}
						}

						if (itemstack1 != null && entity instanceof EntityLivingBase) {
							itemstack1.hitEntity((EntityLivingBase) entity, player);

							if (itemstack1.stackSize <= 0) {
								player.setHeldItem(EnumHand.MAIN_HAND, (ItemStack) null);
								net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, itemstack1, EnumHand.MAIN_HAND);
							}
						}

						if (targetEntity instanceof EntityLivingBase) {
							float f5 = f4 - ((EntityLivingBase) targetEntity).getHealth();
							player.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (j > 0) {
								targetEntity.setFire(j * 4);
							}

							if (player.worldObj instanceof WorldServer && f5 > 2.0F) {
								int k = (int) ((double) f5 * 0.5D);
								((WorldServer) player.worldObj).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (double) (targetEntity.height * 0.5F), targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D, new int[0]);
							}
						}

						player.addExhaustion(0.3F);
					}
					else {
						player.worldObj.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0F, 1.0F);

						if (flag4) {
							targetEntity.extinguish();
						}
					}
				}
			}
		}
	}

	// Amethyst sword deals 1.5x damage to Nether mobs
	@SubscribeEvent
	public void onPlayerAttacking(AttackEntityEvent ev) {
		Entity target = ev.getTarget();
		EntityPlayer player = ev.getEntityPlayer();

		if (player.isServerWorld() && player.getHeldItemMainhand().getItem() == Amethystic.items.AMETHYST_SWORD) {
			if (target.isImmuneToFire() && !(target instanceof EntityShulker) && !(target instanceof EntityDragon)) {
				ev.setCanceled(true);
				attackWithCurrentItemExt(player, target, 2.0f);
			}
		}
	}

	// Auto-smelt with a 10% chance if using the amethyst pickaxe
	@SubscribeEvent
	public void onHarvest(HarvestDropsEvent ev) {
		List<ItemStack> drops = ev.getDrops();
		EntityPlayer player = ev.getHarvester();
		World world = ev.getWorld();
		if (world.rand.nextInt(10) == 1 && player != null && player.getHeldItemMainhand().getItem() == Amethystic.items.AMETHYST_PICKAXE) {
			for (final ListIterator<ItemStack> i = drops.listIterator(); i.hasNext();) {
				final ItemStack element = i.next();
				ItemStack newItem = FurnaceRecipes.instance().getSmeltingResult(element);
				int amount = element.stackSize;
				if (newItem != null) {
					newItem.stackSize = amount;
					i.set(newItem);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBreak(BreakEvent ev) {
		EntityPlayer player = ev.getPlayer();
		IBlockState state = ev.getState();
		BlockPos pos = ev.getPos();
		Block block = state.getBlock();
		ItemStack mainItem = player.getHeldItemMainhand(), offhandItem = player.getHeldItemOffhand();
		World world = ev.getWorld();

		// Mining amethyst ore makes nearby pigmen angry at you
		if (block == Amethystic.blocks.AMETHYST_ORE) {
			List<EntityPigZombie> pigmen = world.getEntitiesWithinAABB(EntityPigZombie.class, new AxisAlignedBB(pos).expand(10, 10, 10));
			for (EntityPigZombie pig : pigmen) {
				pig.setRevengeTarget(player);
			}
		}
		// Amethyst axes can tree-capitate using a BFS, with a 10% chance
		else if ((block == Blocks.LOG || block == Blocks.LOG2) && world.rand.nextInt(10) == 1) {
			if (mainItem != null && mainItem.getItem() == Amethystic.items.AMETHYST_AXE) {

				// Get log variant across two log classes
				PropertyEnum<BlockPlanks.EnumType> variantProp = null;
				if (block == Blocks.LOG) {
					variantProp = BlockOldLog.VARIANT;
				}
				else if (block == Blocks.LOG2) {
					variantProp = BlockNewLog.VARIANT;
				}
				BlockPlanks.EnumType variant = state.getValue(variantProp);

				LinkedList<BlockPos> searchQueue = new LinkedList<BlockPos>();
				searchQueue.add(pos);
				BlockPos current = null;
				BlockPos newBlock = null;
				while (!searchQueue.isEmpty()) {
					current = searchQueue.removeFirst();
					world.destroyBlock(current, true);

					newBlock = current.up();
					if (world.getBlockState(newBlock).getBlock() == block && world.getBlockState(newBlock).getValue(variantProp).equals(variant)) {
						searchQueue.add(newBlock);
					}

					newBlock = current.down();
					if (world.getBlockState(newBlock).getBlock() == block && world.getBlockState(newBlock).getValue(variantProp).equals(variant)) {
						searchQueue.add(newBlock);
					}

					newBlock = current.north();
					if (world.getBlockState(newBlock).getBlock() == block && world.getBlockState(newBlock).getValue(variantProp).equals(variant)) {
						searchQueue.add(newBlock);
					}

					newBlock = current.south();
					if (world.getBlockState(newBlock).getBlock() == block && world.getBlockState(newBlock).getValue(variantProp).equals(variant)) {
						searchQueue.add(newBlock);
					}

					newBlock = current.east();
					if (world.getBlockState(newBlock).getBlock() == block && world.getBlockState(newBlock).getValue(variantProp).equals(variant)) {
						searchQueue.add(newBlock);
					}

					newBlock = current.west();
					if (world.getBlockState(newBlock).getBlock() == block && world.getBlockState(newBlock).getValue(variantProp).equals(variant)) {
						searchQueue.add(newBlock);
					}
				}
			}
		}
		// Amethyst shovel digs 3x3 of diggable blocks!
		else if (mainItem.getItem() == Amethystic.items.AMETHYST_SHOVEL && block.isToolEffective("shovel", state)) {

			// Calculate plane to destroy based on last-known block click face
			// of player
			EnumFacing facing = facingMap.get(player.getPersistentID());
			Axis axis = facing.getAxis();
			int minX = pos.getX(), minY = pos.getY(), minZ = pos.getZ();
			int maxX = pos.getX(), maxY = pos.getY(), maxZ = pos.getZ();
			if (axis == Axis.Y) {
				--minX;
				--minZ;
				++maxX;
				++maxZ;
			}
			else if (axis == Axis.X) {
				--minY;
				--minZ;
				++maxY;
				++maxZ;
			}
			else if (axis == Axis.Z) {
				--minX;
				--minY;
				++maxX;
				++maxY;
			}

			// Destroy plane
			BlockPos current = pos;
			IBlockState currentState = null;
			for (int xx = minX; xx <= maxX; ++xx) {
				for (int yy = minY; yy <= maxY; ++yy) {
					for (int zz = minZ; zz <= maxZ; ++zz) {
						current = new BlockPos(xx, yy, zz);
						currentState = world.getBlockState(current);
						if (currentState.getBlock().isToolEffective("shovel", currentState)) {
							world.destroyBlock(current, true);
							mainItem.damageItem(1, player);
						}
					}
				}
			}
			// END DESTRUCTION
		}

	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent ev) {

		// Make violet and ender flames burn living entities
		EntityLivingBase ent = ev.getEntityLiving();
		World world = ent.getEntityWorld();
		BlockPos pos = ent.getPosition();
		IBlockState state = world.getBlockState(pos);
		if (!ent.isBurning()) {
			if (state.getBlock() == Amethystic.blocks.VIOLET_FLAME) {
				ent.setFire(8);
			}
			else if (state.getBlock() == Amethystic.blocks.ENDER_FLAME) {
				ent.setFire(1);
			}
		}

		// Make amethyst armor reduce negative status effects at the cost of
		// durability; cures negative beacon effects instantly without damage
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ent;
			Iterable<ItemStack> armors = ent.getArmorInventoryList();
			Collection<PotionEffect> effects = player.getActivePotionEffects();
			for (PotionEffect effect : effects) {
				if (!effect.getPotion().isBadEffect()) {
					continue;
				}
				for (ItemStack stack : armors) {
					if (stack == null || !(stack.getItem() instanceof ItemAmethystArmor)) {
						continue;
					}
					if (effect.getDuration() > 0) {
						// Remove negative beacon effects without damage
						if (effect.getIsAmbient()) {
							player.removeActivePotionEffect(effect.getPotion());
							continue;
						}
						else {
							// Shorten negative potion effects while taking
							// armor damage
							try {
								deincPotion.invoke(effect);
								stack.damageItem(1, player);
							}
							catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
							catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLeftClickBlock(LeftClickBlock ev) {
		BlockPos pos = ev.getPos();
		EntityPlayer player = ev.getEntityPlayer();
		World world = ev.getWorld();

		// Allow creative players to click violet flames out
		BlockPos firepos = pos.offset(ev.getFace());
		if (player.canPlayerEdit(firepos, ev.getFace(), player.getHeldItemMainhand()) && (world.getBlockState(firepos).getBlock() == Amethystic.blocks.VIOLET_FLAME || world.getBlockState(firepos).getBlock() == Amethystic.blocks.ENDER_FLAME)) {
			world.setBlockToAir(firepos);
			world.playEvent(player, 1009, firepos, 0);
			ev.setCanceled(true);
		}

		// Keep track of the block side players are clicking for calculations
		// when they break it
		facingMap.put(player.getPersistentID(), ev.getFace());
	}

	// Method to replace the lapis slot on any passed ContainerEnchantment, to
	// allow amethyst; and prevent amethyst from moving into first slot
	private void ReplaceLapisSlots(ContainerEnchantment ench) {
		Slot mainSlot = new Slot(ench.tableInventory, 0, 15, 47) {
			/**
			 * Check if the stack is a valid item for this slot. Always true
			 * beside for the armor slots.
			 */
			public boolean isItemValid(@Nullable ItemStack stack) {
				return (stack.getItem() != Amethystic.items.AMETHYST);
			}

			/**
			 * Returns the maximum stack size for a given slot (usually the same
			 * as getInventoryStackLimit(), but 1 in the case of armor slots)
			 */
			public int getSlotStackLimit() {
				return 1;
			}
		};

		Slot newSlot = new Slot(ench.tableInventory, 1, 35, 47) {
			List<ItemStack> ores = OreDictionary.getOres("gemLapis");

			public boolean isItemValid(@Nullable ItemStack stack) {
				if (stack.getItem() == Amethystic.items.AMETHYST) {
					return true;
				}
				for (ItemStack ore : ores)
					if (OreDictionary.itemMatches(ore, stack, false)) return true;
				return false;
			}
		};
		mainSlot.slotNumber = 0;
		ench.inventorySlots.set(0, mainSlot);
		newSlot.slotNumber = 1;
		ench.inventorySlots.set(1, newSlot);
	}

	// Server-side replacement of lapis slot
	@SubscribeEvent
	public void onOpenContainer(PlayerContainerEvent.Open ev) {
		Container cont = ev.getContainer();
		if (cont instanceof ContainerEnchantment) {
			ContainerEnchantment ench = (ContainerEnchantment) cont;
			ReplaceLapisSlots(ench);
		}
	}

	// Client-side replacement of lapis slot
	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent ev) {
		Gui gui = ev.getGui();
		if (gui instanceof GuiEnchantment) {
			ContainerEnchantment ench = (ContainerEnchantment) ((GuiEnchantment) gui).inventorySlots;
			ReplaceLapisSlots(ench);
		}
	}

	// Track chat messages for the /testforchat command
	private static HashMap<EntityPlayerMP, String> chats = new HashMap<EntityPlayerMP, String>();

	public static String getLastMessage(EntityPlayerMP player) {
		if (!chats.containsKey(player)) {
			return null;
		}
		else {
			return chats.get(player);
		}
	}

	public static void setLastMessage(EntityPlayerMP player, String value) {
		chats.put(player, value);
	}

	@SubscribeEvent
	public void onChat(ServerChatEvent ev) {
		chats.put(ev.getPlayer(), ev.getMessage());
	}
}
