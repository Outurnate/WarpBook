package com.panicnot42.warpbook.commands;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.CommandUtils.ChatType;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

public class GiveWarpCommand extends CommandBase
{
  @Override
  public String getName()
  {
    return "givewarp";
  }

  @Override
  public String getUsage(ICommandSender sender)
  {
    return "/givewarp name [player]";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender var1, String[] var2) throws CommandException
  {
    WarpWorldStorage storage = WarpWorldStorage.get(var1.getEntityWorld());
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
          CommandUtils.showError(var1, I18n.translateToLocal("help.noplayerspecified").trim());
          return;
        }
        break;
      case 2:
        name = var2[0];
        try
        {
          player = CommandBase.getPlayer(server, var1, var2[1]);
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
      CommandUtils.showError(var1, String.format(I18n.translateToLocal("help.waypointdoesnotexist").trim(), name));
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
    return this.getName().compareTo(command.getName());
  }
}
