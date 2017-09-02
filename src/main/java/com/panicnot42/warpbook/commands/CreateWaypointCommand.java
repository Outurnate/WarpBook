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

public class CreateWaypointCommand extends CommandBase
{
  @Override
  public String getName()
  {
    return "waypoint";
  }

  @Override
  public String getUsage(ICommandSender sender)
  {
    return "/waypoint id x y z dim description";
  }
  
  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
  {
    if (args.length < 6)
    {
      CommandUtils.printUsage(sender, this);
      return;
    }
    WarpWorldStorage storage = WarpWorldStorage.get(sender.getEntityWorld());
    try
    {
      Waypoint wp = new Waypoint(CommandUtils.stringConcat(args, 5), args[0], CommandBase.parseInt(args[1]), CommandBase.parseInt(args[2]), CommandBase.parseInt(args[3]), CommandBase.parseInt(args[4]));
      storage.addWaypoint(wp);
      CommandUtils.info(sender, I18n.translateToLocal("help.waypointcreated").trim());
    }
    catch (Exception e)
    {
      CommandUtils.printUsage(sender, this);
    }
    storage.save(sender.getEntityWorld());
  }
  
  public int compareTo(ICommand command)
  {
    return this.getName().compareTo(command.getName());
  }
}
