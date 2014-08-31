package com.panicnot42.warpbook.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtils
{
  public static EntityPlayer getPlayerByUUID(UUID uuid)
  {
    for(Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
      if (((EntityPlayerMP)player).getGameProfile().getId().equals(uuid))
        return (EntityPlayer)player;
    return null;
  }

  public static boolean isPlayerOnline(UUID uuid)
  {
    return getPlayerByUUID(uuid) != null;
  }
  
  public static String getNameByUUID(UUID uuid)
  {
    return MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid).getName();
  }
}
