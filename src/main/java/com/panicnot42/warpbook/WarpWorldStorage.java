package com.panicnot42.warpbook;

import io.netty.channel.ChannelFutureListener;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class WarpWorldStorage extends WorldSavedData
{
  public static HashMap<String, Waypoint> table;
  public static HashMap<UUID, Waypoint> deaths;
  public static ArrayList<GameProfile> profiles;

  private final static String IDENTIFIER = "WarpBook";

  public WarpWorldStorage(String identifier)
  {
    super(identifier);
  }

  public static WarpWorldStorage instance(World world)
  {
    if (world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER) == null) world.mapStorage.setData(IDENTIFIER, new WarpWorldStorage(IDENTIFIER));
    WarpWorldStorage storage = (WarpWorldStorage)world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER);
    return storage;
  }

  public static void postInit()
  {
    table = new HashMap<String, Waypoint>();
    deaths = new HashMap<UUID, Waypoint>();
    profiles = new ArrayList<GameProfile>();
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    table  = NBTUtils.readHashMapFromNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), Waypoint.class);
    HashMap<String, Waypoint> deaths = NBTUtils.readHashMapFromNBT(var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND), Waypoint.class);
    WarpWorldStorage.deaths = new HashMap<UUID, Waypoint>();
    for (Entry<String, Waypoint> death : deaths.entrySet())
      WarpWorldStorage.deaths.put(UUID.fromString(death.getKey()), death.getValue());
    NBTTagList players = var1.getTagList("players", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < players.tagCount(); ++i)
    {
      NBTTagCompound tag = players.getCompoundTagAt(i);
      profiles.add(new GameProfile(new UUID(tag.getLong("least"), tag.getLong("most")), tag.getString("name")));
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTUtils.writeHashMapToNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), table);
    HashMap<String, Waypoint> deaths = new HashMap<String, Waypoint>();
    for (Entry<UUID, Waypoint> death : WarpWorldStorage.deaths.entrySet())
      deaths.put(death.getKey().toString(), death.getValue());
    NBTUtils.writeHashMapToNBT(var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND), deaths);
    NBTTagList players = new NBTTagList();
    for (GameProfile profile : profiles)
    {
      NBTTagCompound profTag = new NBTTagCompound();
      profTag.setLong("least", profile.getId().getLeastSignificantBits());
      profTag.setLong("most",  profile.getId().getMostSignificantBits());
      profTag.setString("name", profile.getName());
    }
    var1.setTag("players", players);
  }

  void updateClient(EntityPlayerMP player, ServerConnectionFromClientEvent e)
  {
    FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel(Properties.modid, Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DISPATCHER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(e.manager));
    channel.writeAndFlush(new PacketSyncWaypoints(table)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    //WarpBookMod.network.sendTo(new PacketSyncWaypoints(table), player);
  }

  public boolean waypointExists(String name)
  {
    return table.containsKey(name);
  }

  public Waypoint getWaypoint(String name)
  {
    return table.get(name);
  }

  public void addWaypoint(Waypoint point)
  {
    table.put(point.name, point);
    this.markDirty();
  }

  public String[] listWaypoints()
  {
    return (String[])table.keySet().toArray();
  }

  public boolean deleteWaypoint(String waypoint)
  {
    this.markDirty();
    return table.remove(waypoint) != null;
  }

  public void setLastDeath(UUID id, double posX, double posY, double posZ, int dim)
  {
    deaths.put(id, new Waypoint("Death Point", "death", MathUtils.round(posX, RoundingMode.DOWN), MathUtils.round(posY, RoundingMode.DOWN), MathUtils.round(posZ, RoundingMode.DOWN), dim));
    this.markDirty();
  }
  
  public void clearLastDeath(UUID id)
  {
    deaths.remove(id);
    this.markDirty();
  }

  public static Waypoint getLastDeath(UUID id)
  {
    return deaths.get(id);
  }
}
