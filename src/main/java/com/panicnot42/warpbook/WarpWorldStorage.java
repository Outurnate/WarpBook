package com.panicnot42.warpbook;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

public class WarpWorldStorage extends WorldSavedData
{
  public static HashMap<String, Waypoint> table;
  public static HashMap<UUID, Waypoint> deaths;

  private final static String IDENTIFIER = "WarpBook";

  public WarpWorldStorage(String identifier)
  {
    super(identifier);
  }

  public static WarpWorldStorage instance(World world)
  {
    if (world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER) == null) world.mapStorage.setData(IDENTIFIER, new WarpWorldStorage(IDENTIFIER));
    WarpWorldStorage storage = (WarpWorldStorage)world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER);
    MinecraftForge.EVENT_BUS.register(storage);
    return storage;
  }

  public static void postInit()
  {
    table = new HashMap<String, Waypoint>();
    deaths = new HashMap<UUID, Waypoint>();
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    table  = NBTUtils.readHashMapFromNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), Waypoint.class);
    HashMap<String, Waypoint> deaths = NBTUtils.readHashMapFromNBT(var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND), Waypoint.class);
    WarpWorldStorage.deaths = new HashMap<UUID, Waypoint>();
    for (Entry<String, Waypoint> death : deaths.entrySet())
      WarpWorldStorage.deaths.put(UUID.fromString(death.getKey()), death.getValue());
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTUtils.writeHashMapToNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), table);
    HashMap<String, Waypoint> deaths = new HashMap<String, Waypoint>();
    for (Entry<UUID, Waypoint> death : WarpWorldStorage.deaths.entrySet())
      deaths.put(death.getKey().toString(), death.getValue());
    NBTUtils.writeHashMapToNBT(var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND), deaths);
  }
  
  @SubscribeEvent
  public void clientJoined(ServerConnectionFromClientEvent e)
  {
    WarpBookMod.network.sendTo(new PacketSyncWaypoints(table), ((NetHandlerPlayServer)e.handler).playerEntity);
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
