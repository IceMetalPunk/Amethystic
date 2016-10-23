package com.IceMetalPunk.amethystic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats.Type;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandClearTags extends CommandBase implements ICommand {

	private final List<String> aliases = new ArrayList<String>();

	public CommandClearTags() {
		aliases.add("cleartags");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "cleartags";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.cleartags.usage";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 2) {
			throw new WrongUsageException("commands.cleartags.usage", new Object[0]);
		}
		List<Entity> ents = new ArrayList<Entity>();
		if (args.length <= 0) {
			ents.add(sender.getCommandSenderEntity());
		}
		else {
			ents = getEntityList(server, sender, args[0]);
		}

		if (ents == null || ents.isEmpty()) {
			throw new CommandException("commands.cleartags.noentity", new Object[0]);
		}

		int number = 0;

		// Prefix-matched removal
		List<String> toRemove = new ArrayList<String>();
		if (args.length > 1) {
			String prefix = args[1];
			for (Entity ent : ents) {
				Set<String> tags = ent.getTags();
				for (String tag : tags) {
					if (tag.startsWith(prefix)) {
						toRemove.add(tag);
					}
				}

				for (String tag : toRemove) {
					tags.remove(tag);
					++number;
				}
			}
		}

		// Generic removal
		else {
			for (Entity ent : ents) {
				Set<String> tags = ent.getTags();
				number += tags.size();
				tags.clear();
			}
		}
		sender.setCommandStat(Type.SUCCESS_COUNT, number);
		if (number <= 0) {
			throw new CommandException("commands.cleartags.notags", new Object[0]);
		}
		notifyCommandListener(sender, this, "commands.cleartags.success", new Object[] { number, ents.size() });
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
