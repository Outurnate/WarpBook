package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class DeleteWaypointCommand extends CommandBase
{
  @Override
  public String getName()
  {
    return "waypointdelete";
  }

  @Override
  public String getUsage(ICommandSender sender)
  {
    return "/waypointdelete name";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender var1, String[] var2) throws CommandException
  {
    WarpWorldStorage storage = WarpWorldStorage.get(var1.getEntityWorld());
    if (var2.length != 1)
    {
      CommandUtils.printUsage(var1, this);
      return;
    }
    if (storage.deleteWaypoint(var2[0]))
      CommandUtils.info(var1, I18n.translateToLocal("help.waypointdelete").trim());
    else
      CommandUtils.showError(var1, net.minecraft.client.resources.I18n.format(I18n.translateToLocal("help.notawaypoint").trim(), var2[0]));
    storage.save(var1.getEntityWorld());
  }
  
  public int compareTo(ICommand command)
  {
    return this.getName().compareTo(command.getName());
  }
}
