package com.panicnot42.warpbook.util;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.net.packet.PacketSyncPlayers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtils
{
  private ArrayList<GameProfile> profiles = new ArrayList<GameProfile>();
  private ReentrantLock lock = new ReentrantLock();
  
  private static PlayerUtils instance;
  
  public static PlayerUtils instance()
  {
    return (instance == null) ? instance = new PlayerUtils() : instance;
  }
  
  private PlayerUtils()
  {
  }

  public void updateClient(EntityPlayerMP player)
  {
    lock.lock();
    profiles.add(player.getGameProfile());
    WarpWorldStorage.profiles.add(player.getGameProfile());
    WarpWorldStorage.instance(player.worldObj).markDirty();
    WarpBookMod.network.sendTo(new PacketSyncPlayers(profiles), player);
    lock.unlock();
  }

  public void removeClient(EntityPlayerMP player)
  {
    lock.lock();
    profiles.remove(player.getGameProfile());
    lock.unlock();
  }
  
  public static EntityPlayer getPlayerByUUID(UUID uuid)
  {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
      for(Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
        if (((EntityPlayerMP)player).getGameProfile().getId().equals(uuid))
          return (EntityPlayer)player;
    return null;
  }

  public static boolean isPlayerOnline(UUID uuid)
  {
    instance().lock.lock();
    for (GameProfile profile : instance().profiles)
      if (profile.getId().equals(uuid))
      {
        instance().lock.unlock();
        return true;
      }
    instance().lock.unlock();
    return false;
  }
  
  public static String getNameByUUID(UUID uuid)
  {
    instance().lock.lock();
    for (GameProfile profile : WarpWorldStorage.profiles)
      if (profile.getId().equals(uuid))
      {
        instance().lock.unlock();
        return profile.getName();
      }
    instance().lock.unlock();
    return "Unknown Name";
  }

  public void setProfiles(ArrayList<GameProfile> profiles)
  {
    lock.lock();
    this.profiles = profiles;
    lock.unlock();
  }
}
