package com.panicnot42.warpbook.util;

import io.netty.channel.ChannelFutureListener;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.Properties;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.net.packet.PacketSyncPlayers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;

public class PlayerUtils
{
  private ArrayList<GameProfile> profiles = new ArrayList<GameProfile>();

  private static PlayerUtils instance;

  public static PlayerUtils instance()
  {
    return (instance == null) ? instance = new PlayerUtils() : instance;
  }

  private PlayerUtils()
  {
  }

  public void updateClient(EntityPlayerMP player, ServerConnectionFromClientEvent e)
  {
    profiles.add(player.getGameProfile());
    WarpWorldStorage.profiles.add(player.getGameProfile());
    WarpWorldStorage.instance(player.worldObj).markDirty();
    FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel(Properties.modid, Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DISPATCHER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(e.manager));
    channel.writeAndFlush(new PacketSyncPlayers(profiles)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    // WarpBookMod.network.sendTo(new PacketSyncPlayers(profiles), player);
  }

  public void removeClient(EntityPlayerMP player)
  {
    profiles.remove(player.getGameProfile());
  }

  public static EntityPlayer getPlayerByUUID(UUID uuid)
  {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
      if (((EntityPlayerMP)player).getGameProfile().getId().equals(uuid)) return (EntityPlayer)player;
    return null;
  }

  public static boolean isPlayerOnline(UUID uuid)
  {
    for (GameProfile profile : instance().profiles)
      if (profile.getId().equals(uuid)) return true;
    return false;
  }

  public static String getNameByUUID(UUID uuid)
  {
    for (GameProfile profile : WarpWorldStorage.profiles)
      if (profile.getId().equals(uuid)) return profile.getName();
    return "Unknown Name";
  }

  public void setProfiles(ArrayList<GameProfile> profiles)
  {
    this.profiles = profiles;
  }
}
