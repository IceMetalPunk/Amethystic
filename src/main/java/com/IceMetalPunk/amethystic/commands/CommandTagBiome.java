package com.IceMetalPunk.amethystic.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTagBiome extends CommandBase implements ICommand {

	private final List<String> aliases = new ArrayList<String>();

	public CommandTagBiome() {
		aliases.add("tagbiome");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "tagbiome";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.tagbiome.usage";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 2) {
			throw new WrongUsageException("commands.tagbiome.usage", new Object[0]);
		}

		String prefix = "";
		if (args.length > 1) {
			prefix = args[1];
		}

		List<Entity> ents = new ArrayList<Entity>();
		if (args.length <= 0) {
			ents.add(sender.getCommandSenderEntity());
		}
		else {
			ents = getEntityList(server, sender, args[0]);
		}

		for (Entity ent : ents) {
			String biome = ent.worldObj.getBiomeForCoordsBody(ent.getPosition()).getBiomeName();
			ent.addTag(prefix + biome);
		}
		notifyCommandListener(sender, this, "commands.tagbiome.success", new Object[0]);

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
