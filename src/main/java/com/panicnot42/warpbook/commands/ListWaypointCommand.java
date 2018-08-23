package com.panicnot42.warpbook.commands;

import java.util.ArrayList;
import java.util.List;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class ListWaypointCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "waypointlist";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/waypointlist [page]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		WarpWorldStorage storage = WarpWorldStorage.get(sender.getEntityWorld());
		int page;
		try {
			page = args.length == 0 ? 0 : CommandBase.parseInt(args[0]);
			List<String> wps = new ArrayList<String>(storage.listWaypoints());
			if (wps.size() == 0) {
				CommandUtils.showError(sender, I18n.translateToLocal("help.nowaypointsfound").trim());
				return;
			}
			CommandUtils.info(sender, String.format("-- Page %d --", page));
			for (int i = page * 8; i < ((page * 8) + 9); ++i) {
				try {
					Waypoint wp = storage.getWaypoint(wps.get(i));
					CommandUtils.info(sender, wp.name + " [ " + wp.x + ", " +  wp.y + ", " + wp.z +  " ] Dim: " + wp.dim + ", Info: \"" + wp.friendlyName + "\"" );
				}
				catch (IndexOutOfBoundsException e) {
				}
			}
			CommandUtils.info(sender, String.format("-- Page %d --", page));
		}
		catch (Exception e)
		{
			CommandUtils.printUsage(sender, this);
			CommandUtils.showError(sender, CommandUtils.ChatType.TYPE_int, e.getLocalizedMessage());
		}
	}

	public int compareTo(ICommand command) {
		return this.getName().compareTo(command.getName());
	}


}
