package com.IceMetalPunk.amethystic.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandLight extends CommandBase implements ICommand {

	private final List<String> aliases = new ArrayList<String>();

	public CommandLight() {
		aliases.add("light");
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "light";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "commands.light.usage";
	}

	@Override
	public List<String> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 4) {
			throw new WrongUsageException("commands.light.usage", new Object[0]);
		}
		CommandBase.CoordinateArg x = CommandBase.parseCoordinate(sender.getPosition().getX(), args[1], false);
		CommandBase.CoordinateArg y = CommandBase.parseCoordinate(sender.getPosition().getY(), args[2], false);
		CommandBase.CoordinateArg z = CommandBase.parseCoordinate(sender.getPosition().getZ(), args[3], false);
		BlockPos pos = new BlockPos(x.getResult(), y.getResult(), z.getResult());
		// int lightLevel =
		// sender.getEntityWorld().getLightFor(EnumSkyBlock.SKY, pos);
		int lightLevel = sender.getEntityWorld().getLight(pos, true);
		if ("test".equalsIgnoreCase(args[0])) {
			if (args.length < 5 || args.length > 6) {
				throw new WrongUsageException("commands.light.test.usage", new Object[0]);
			}
			int min = (args.length >= 5 && !args[4].equals("*")) ? Integer.valueOf(args[4]) : Integer.MIN_VALUE;
			int max = (args.length >= 6 && !args[5].equals("*")) ? Integer.valueOf(args[5]) : Integer.MAX_VALUE;
			if (min > lightLevel || max < lightLevel) {
				throw new CommandException("commands.light.test.failed", new Object[] { x.getResult(), y.getResult(),
						z.getResult(), lightLevel, min, max });
			}
			else {
				notifyCommandListener(sender, this, "commands.light.test.success", new Object[] { x.getResult(),
						y.getResult(), z.getResult(), lightLevel, min, max });
			}

		}
		else if ("query".equalsIgnoreCase(args[0])) {
			if (args.length != 4) {
				throw new WrongUsageException("commands.light.test.usage", new Object[0]);
			}
			sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, lightLevel);
			notifyCommandListener(sender, this, "commands.light.query", new Object[] { x.getResult(), y.getResult(),
					z.getResult(), lightLevel });
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, "query", "test");
		}
		else if (args.length >= 2 && args.length <= 4) {
			return getTabCompletionCoordinate(args, 1, pos);
		}
		return new ArrayList<String>();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
