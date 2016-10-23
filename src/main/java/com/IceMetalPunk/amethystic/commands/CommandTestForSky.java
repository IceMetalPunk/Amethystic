package com.IceMetalPunk.amethystic.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTestForSky extends CommandBase implements ICommand {

	private final List<String> aliases = new ArrayList<String>();

	public CommandTestForSky() {
		aliases.add("testforsky");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "testforsky";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.testforsky.usage";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 3) {
			throw new WrongUsageException("commands.testforsky.usage", new Object[0]);
		}
		CommandBase.CoordinateArg x = CommandBase.parseCoordinate(sender.getPosition().getX(), args[0], false);
		CommandBase.CoordinateArg y = CommandBase.parseCoordinate(sender.getPosition().getY(), args[1], false);
		CommandBase.CoordinateArg z = CommandBase.parseCoordinate(sender.getPosition().getZ(), args[2], false);
		BlockPos pos = new BlockPos(x.getResult(), y.getResult(), z.getResult());
		boolean canSeeSky = sender.getEntityWorld().canBlockSeeSky(pos);
		if (!canSeeSky) {
			throw new CommandException("commands.testforsky.failed", new Object[] { x.getResult(), y.getResult(),
					z.getResult() });
		}
		else {
			notifyCommandListener(sender, this, "commands.testforsky.success", new Object[] { x.getResult(),
					y.getResult(), z.getResult() });
		}

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if (args.length >= 1 && args.length <= 3) {
			return getTabCompletionCoordinate(args, 0, pos);
		}
		return new ArrayList<String>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
