package panicnot42.warpbook.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CreateWaypointCommand extends CommandBase
{
  @Override
  public String getCommandName()
  {
    return "waypoint";
  }

  @Override
  public String getCommandUsage(ICommandSender var1)
  {
    return "/waypoint id x y z dim description";
  }

  @Override
  public void processCommand(ICommandSender var1, String[] var2)
  {
    if (var2.length != 6)
      return;
    
  }
}
