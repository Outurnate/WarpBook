package panicnot42.warpbook;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

public class WarpWorldStorage extends WorldSavedData
{
  public class Waypoint
  {
    public String name;
    public int x, y, z, dim;
  }
  
  HashMap<String, Waypoint> waypoints;
  
  public WarpWorldStorage()
  {
    super("WarpBook");
    waypoints = new HashMap<String, Waypoint>();
  }

  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    NBTTagList tags = var1.getTagList("waypoints", new NBTTagCompound().getId());
    for (int i = 0; i < tags.tagCount(); ++i)
    {
      Waypoint wp = new Waypoint();
      wp.name = tags.getCompoundTagAt(i).getString("friendlyName");
      wp.x = tags.getCompoundTagAt(i).getInteger("x");
      wp.y = tags.getCompoundTagAt(i).getInteger("y");
      wp.z = tags.getCompoundTagAt(i).getInteger("z");
      wp.dim = tags.getCompoundTagAt(i).getInteger("dim");
      waypoints.put(tags.getCompoundTagAt(i).getString("name"), wp);
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    @SuppressWarnings("unchecked")
    Entry<String, Waypoint>[] wps = (Entry<String, Waypoint>[])waypoints.entrySet().toArray();
    NBTTagList tags = new NBTTagList();
    for (int i = 0; i < wps.length; ++i)
    {
      NBTTagCompound wp = new NBTTagCompound();
      wp.setString("friendlyName", wps[i].getValue().name);
      wp.setInteger("x", wps[i].getValue().x);
      wp.setInteger("y", wps[i].getValue().y);
      wp.setInteger("z", wps[i].getValue().z);
      wp.setInteger("dim", wps[i].getValue().dim);
      wp.setString("name", wps[i].getKey());
      tags.appendTag(wp);
    }
    var1.setTag("waypoints", tags);
  }
}
