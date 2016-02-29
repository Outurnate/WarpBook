package com.panicnot42.warpbook.commands;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;

public class DeleteWaypointCommand extends CommandBase
{

    @Override
    public String getCommandName() {
        return "waypointdelete";
    }

    @Override
  public String getCommandUsage(ICommandSender var1)
  {
    return "/waypointdelete name";
  }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
    WarpWorldStorage storage = WarpWorldStorage.instance(var1.getEntityWorld());
    if (var2.length != 1)
    {
      CommandUtils.printUsage(var1, this);
      return;
    }
    if (storage.deleteWaypoint(var2[0]))
      CommandUtils.info(var1, I18n.format("help.waypointdelete"));
    else
      CommandUtils.showError(var1, I18n.format("help.notawaypoint", var2[0]));
  }

  public int compareTo(ICommand command)
  {
    return this.getCommandName().compareTo(command.getCommandName());
  }
}
