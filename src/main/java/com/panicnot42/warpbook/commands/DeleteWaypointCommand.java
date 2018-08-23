package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class DeleteWaypointCommand extends CommandBase {

	@Override
	public String getName() {
		return "waypointdelete";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/waypointdelete name";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	//public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		WarpWorldStorage storage = WarpWorldStorage.get(sender.getEntityWorld());
		if (args.length != 1)
		{
			CommandUtils.printUsage(sender, this);
			return;
		}
		if (storage.deleteWaypoint(args[0]))
			CommandUtils.info(sender, I18n.translateToLocal("help.waypointdelete").trim());
		else
			CommandUtils.showError(sender, net.minecraft.client.resources.I18n.format(I18n.translateToLocal("help.notawaypoint").trim(), args[0]));
		storage.save(sender.getEntityWorld());
	}

	public int compareTo(ICommand command) {
		return this.getName().compareTo(command.getName());
	}
	
}
