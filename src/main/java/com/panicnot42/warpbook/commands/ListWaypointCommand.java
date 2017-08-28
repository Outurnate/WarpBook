package com.panicnot42.warpbook.commands;

import java.util.ArrayList;
import java.util.List;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class ListWaypointCommand extends CommandBase
{
  @Override
  public String getCommandName()
  {
    return "waypointlist";
  }
  
  @Override
  public String getCommandUsage(ICommandSender var1)
  {
    return "/waypointlist [page]";
  }
  
  @Override
  public void execute(MinecraftServer server, ICommandSender var1, String[] var2) throws CommandException
  {
    WarpWorldStorage storage = WarpWorldStorage.get(var1.getEntityWorld());
    int page;
    try
    {
      page = var2.length == 0 ? 0 : CommandBase.parseInt(var2[0]);
      List<String> wps = new ArrayList<String>(storage.listWaypoints());
      if (wps.size() == 0)
      {
        CommandUtils.showError(var1, I18n.translateToLocal("help.nowaypointsfound").trim());
        return;
      }
      CommandUtils.info(var1, String.format("-- Page %d --", page));
      for (int i = page * 8; i < ((page * 8) + 9); ++i)
      {
        try
        {
          CommandUtils.info(var1, wps.get(i));
        }
        catch (IndexOutOfBoundsException e)
        {
        }
      }
      CommandUtils.info(var1, String.format("-- Page %d --", page));
    }
    catch (Exception e)
    {
      CommandUtils.printUsage(var1, this);
      CommandUtils.showError(var1, CommandUtils.ChatType.TYPE_int, e.getLocalizedMessage());
    }
  }

  public int compareTo(ICommand command)
  {
    return this.getCommandName().compareTo(command.getCommandName());
  }
}
