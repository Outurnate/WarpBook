package com.panicnot42.warpbook;

import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.net.SyncableTable;
import com.panicnot42.warpbook.util.net.UpdateTableEvent;
import com.panicnot42.warpbook.util.net.UpdateTableListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WarpWorldStorage extends WorldSavedData
{
  private static SyncableTable<Waypoint> table;

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
    table = new SyncableTable<Waypoint>(Waypoint.class, "waypointTable");
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    table.readFromNBT(var1);
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    table.writeToNBT(var1);
  }

  public boolean waypointExists(String name)
  {
    return table.contains(name);
  }

  public Waypoint getWaypoint(String name)
  {
    return table.get(name);
  }

  public void addWaypoint(Waypoint point)
  {
    table.set(point.name, point);
    this.markDirty();
  }

  public String[] listWaypoints()
  {
    return table.keyList();
  }

  public boolean deleteWaypoint(String waypoint)
  {
    this.markDirty();
    return table.remove(waypoint) != null;
  }
}
