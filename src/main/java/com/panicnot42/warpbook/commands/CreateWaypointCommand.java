package com.panicnot42.warpbook.commands;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.Waypoint;

public class CreateWaypointCommand extends CommandBase
{

    @Override
    public String getCommandName() {
        return "waypoint";
    }

    @Override
  public String getCommandUsage(ICommandSender var1)
  {
    return "/waypoint id x y z dim description";
  }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
    if (var2.length < 6)
    {
      CommandUtils.printUsage(var1, this);
      return;
    }
    WarpWorldStorage storage = WarpWorldStorage.instance(var1.getEntityWorld());
    try
    {
      Waypoint wp = new Waypoint(CommandUtils.stringConcat(var2, 5), var2[0], 0, 0, 0, 0);//TODO CommandBase.parseInt(var1, var2[1]), CommandBase.parseInt(var1, var2[2]), CommandBase.parseInt(var1, var2[3]),
      //    CommandBase.parseInt(var1, var2[4]));
      storage.addWaypoint(wp);
      CommandUtils.info(var1, I18n.format("help.waypointcreated"));
    }
    catch (Exception e)
    {
      CommandUtils.printUsage(var1, this);
//      CommandUtils.showError(var1, CommandUtils.ChatType.TYPE_int, ((String)e.getErrorOjbects()[0]));
    }
  }

  @Override
  public int compareTo(Object o)
  {
    return 42; // TODO: actually implement this
  }
}
