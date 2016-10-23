package com.IceMetalPunk.amethystic.commands;

import java.util.ArrayList;
import java.util.List;

import com.IceMetalPunk.amethystic.events.AmethysticEventHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class CommandTestForChat extends CommandBase implements ICommand {

	private final List<String> aliases = new ArrayList<String>();

	public CommandTestForChat() {
		aliases.add("testforchat");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "testforchat";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testforchat.usage";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 3) {
			throw new WrongUsageException("commands.testforchat.usage", new Object[0]);
		}

		ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 2, !(sender instanceof EntityPlayer));
		String testFor = itextcomponent.getUnformattedText();
		List<Entity> ents = new ArrayList<Entity>();
		if (args.length <= 0) {
			ents.add(sender.getCommandSenderEntity());
		}
		else {
			ents = getEntityList(server, sender, args[0]);
		}

		List<EntityPlayerMP> successes = new ArrayList<EntityPlayerMP>();
		for (Entity ent : ents) {
			if (ent instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) ent;
				String lastChat = AmethysticEventHandler.getLastMessage(player);
				if (lastChat != null && lastChat.equalsIgnoreCase(testFor)) {
					successes.add(player);
					if ("true".equalsIgnoreCase(args[1]) || "1".equals(args[1])) {
						AmethysticEventHandler.setLastMessage(player, "");
					}
				}
			}

		}

		sender.setCommandStat(Type.SUCCESS_COUNT, successes.size());
		if (successes.size() > 0) {
			notifyCommandListener(sender, this, "commands.testforchat.success", new Object[0]);
		}
		else {
			throw new CommandException("commands.testforchat.failed", new Object[0]);
		}

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		return new ArrayList<String>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
