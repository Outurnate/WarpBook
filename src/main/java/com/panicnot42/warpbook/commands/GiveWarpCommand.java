package com.panicnot42.warpbook.commands;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.CommandUtils.ChatType;

public class GiveWarpCommand extends CommandBase
{
  @Override
  public String getCommandName()
  {
    return "givewarp";
  }

  @Override
  public String getCommandUsage(ICommandSender var1)
  {
    return "/givewarp name [player]";
  }

  @Override
  public void processCommand(ICommandSender var1, String[] var2) throws CommandException
  {
    WarpWorldStorage storage = WarpWorldStorage.instance(var1.getEntityWorld());
    String name;
    EntityPlayer player;
    switch (var2.length)
    {
      case 1:
        name = var2[0];
        if (var1 instanceof EntityPlayer)
          player = (EntityPlayer)var1;
        else
        {
          CommandUtils.showError(var1, I18n.format("help.noplayerspecified"));
          return;
        }
        break;
      case 2:
        name = var2[0];
        try
        {
          player = CommandBase.getPlayer(var1, var2[1]);
        }
        catch (PlayerNotFoundException e)
        {
          CommandUtils.showError(var1, ChatType.TYPE_player, var2[1]);
          return;
        }
        break;
      default:
        CommandUtils.printUsage(var1, this);
        return;
    }
    if (!storage.waypointExists(name))
    {
      CommandUtils.showError(var1, I18n.format("help.waypointdoesnotexist", name));
      return;
    }
    ItemStack hyperStack = new ItemStack(WarpBookMod.items.hyperWarpPageItem);
    NBTTagCompound compound = new NBTTagCompound();
    compound.setString("hypername", name);
    hyperStack.setTagCompound(compound);
    player.inventory.addItemStackToInventory(hyperStack);
  }

  public int compareTo(ICommand command)
  {
    return this.getCommandName().compareTo(command.getCommandName());
  }
}
