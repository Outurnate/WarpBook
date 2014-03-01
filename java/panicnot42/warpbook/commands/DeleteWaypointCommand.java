package panicnot42.warpbook.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import panicnot42.util.CommandUtils;
import panicnot42.warpbook.WarpWorldStorage;

public class DeleteWaypointCommand extends CommandBase
{
  @Override
  public String getCommandName()
  {
    return "waypointdelete";
  }

  @Override
  public String getCommandUsage(ICommandSender var1)
  {
    return "/waypointdelete name";
  }

  @Override
  public void processCommand(ICommandSender var1, String[] var2)
  {
    WarpWorldStorage storage = WarpWorldStorage.instance(var1.getEntityWorld());
    if (var2.length != 1)
    {
      CommandUtils.printUsage(var1, this);
      return;
    }
    if (storage.deleteWaypoint(var2[0]))
      CommandUtils.info(var1, "Poof!");
    else
      CommandUtils.showError(var1, String.format("'%s' is not a waypoint", var2[0]));
  }
}
