package com.panicnot42.warpbook;

import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.net.SyncableTable;
import com.panicnot42.warpbook.util.net.UpdateTableEvent;
import com.panicnot42.warpbook.util.net.UpdateTableListener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WarpWorldStorage extends WorldSavedData implements UpdateTableListener
{
  private static SyncableTable<Waypoint> table;
  // private static HashMap<String, Waypoint> waypoints;

  private final static String IDENTIFIER = "WarpBook";

  public WarpWorldStorage(String identifier)
  {
    super(identifier);
    // waypoints = new HashMap<String, Waypoint>();
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
    /*
     * NBTTagList tags = var1.getTagList("waypoints", new
     * NBTTagCompound().getId()); for (int i = 0; i < tags.tagCount(); ++i)
     * table.set(tags.getCompoundTagAt(i).getString("name"), new
     * Waypoint(tags.getCompoundTagAt(i)));
     */
    table.readFromNBT(var1);
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    /*
     * NBTTagList tags = new NBTTagList(); for (Entry<String, Waypoint> wps :
     * waypoints.entrySet()) { NBTTagCompound wp = new NBTTagCompound();
     * wp.setString("friendlyName", wps.getValue().name); wp.setInteger("x",
     * wps.getValue().x); wp.setInteger("y", wps.getValue().y);
     * wp.setInteger("z", wps.getValue().z); wp.setInteger("dim",
     * wps.getValue().dim); wp.setString("name", wps.getKey());
     * tags.appendTag(wp); } var1.setTag("waypoints", tags);
     */
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

  @Override
  public void tableUpdated(UpdateTableEvent updateTableEvent)
  {
    // nope
  }
}
