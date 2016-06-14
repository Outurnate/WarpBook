package com.panicnot42.warpbook;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;

import io.netty.channel.ChannelFutureListener;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

public class WarpWorldStorage extends WorldSavedData
{
  public HashMap<String, Waypoint> table;
  public HashMap<UUID, Waypoint> deaths;

  private final static String IDENTIFIER = "WarpBook";

  public WarpWorldStorage(String identifier)
  {
    super(identifier);
    table = new HashMap<String, Waypoint>();
    deaths = new HashMap<UUID, Waypoint>();
  }

  public static WarpWorldStorage instance(World world)
  {
    if (world.getMapStorage().loadData(WarpWorldStorage.class, IDENTIFIER) == null)
      world.getMapStorage().setData(IDENTIFIER, new WarpWorldStorage(IDENTIFIER));
    WarpWorldStorage storage = (WarpWorldStorage)world.getMapStorage().loadData(WarpWorldStorage.class, IDENTIFIER);
    return storage;
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    table = NBTUtils.readHashMapFromNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), Waypoint.class);
    HashMap<String, Waypoint> deathsNBT = NBTUtils.readHashMapFromNBT(var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND), Waypoint.class);
    for (Entry<String, Waypoint> death : deathsNBT.entrySet())
      deaths.put(UUID.fromString(death.getKey()), death.getValue());
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTUtils.writeHashMapToNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), table);
    HashMap<String, Waypoint> deathsNBT = new HashMap<String, Waypoint>();
    for (Entry<UUID, Waypoint> death : deaths.entrySet())
      deathsNBT.put(death.getKey().toString(), death.getValue());
    NBTUtils.writeHashMapToNBT(var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND), deathsNBT);
  }

  void updateClient(EntityPlayerMP player, ServerConnectionFromClientEvent e)
  {
    FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel(Properties.modid, Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DISPATCHER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(e.manager));
    channel.writeAndFlush(new PacketSyncWaypoints(table)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
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
    boolean res = table.remove(waypoint) != null;
    this.markDirty();
    return res;
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

  public Waypoint getLastDeath(UUID id)
  {
    return deaths.get(id);
  }
}
