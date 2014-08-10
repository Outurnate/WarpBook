package com.panicnot42.warpbook;

import java.util.HashMap;

import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class WarpWorldStorage extends WorldSavedData
{
  private static HashMap<String, Waypoint> table;

  private final static String IDENTIFIER = "WarpBook";

  public WarpWorldStorage(String identifier)
  {
    super(identifier);
  }

  public static WarpWorldStorage instance(World world)
  {
    if (world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER) == null) world.mapStorage.setData(IDENTIFIER, new WarpWorldStorage(IDENTIFIER));
    return (WarpWorldStorage)world.mapStorage.loadData(WarpWorldStorage.class, IDENTIFIER);
  }

  public static void postInit()
  {
    table = new HashMap<String, Waypoint>();
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    table = NBTUtils.readHashMapFromNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), Waypoint.class);
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTUtils.writeHashMapToNBT(var1.getTagList("data", Constants.NBT.TAG_COMPOUND), table);
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
}
