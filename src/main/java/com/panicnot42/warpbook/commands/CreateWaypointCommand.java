package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class CreateWaypointCommand extends CommandBase {

	@Override
	public String getName() {
		return "waypoint";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/waypoint id x y z dim description";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	//public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 6) {
			CommandUtils.printUsage(sender, this);
			return;
		}
		WarpWorldStorage storage = WarpWorldStorage.get(sender.getEntityWorld());
		try {
			String friendlyName = CommandUtils.stringConcat(args, 5);
			int x = CommandBase.parseInt(args[1]);
			int y = CommandBase.parseInt(args[2]);
			int z = CommandBase.parseInt(args[3]);
			int dim = CommandBase.parseInt(args[4]);
			Waypoint wp = new Waypoint(friendlyName, args[0], x, y, z, dim);
			storage.addWaypoint(wp);
			CommandUtils.info(sender, I18n.translateToLocal("help.waypointcreated").trim());
		}
		catch (Exception e) {
			CommandUtils.printUsage(sender, this);
			// CommandUtils.showError(var1, CommandUtils.ChatType.TYPE_int, ((String)e.getErrorOjbects()[0]));
		}
		storage.save(sender.getEntityWorld());
	}
	
	public int compareTo(ICommand command) {
		return this.getName().compareTo(command.getName());
	}
	
}
