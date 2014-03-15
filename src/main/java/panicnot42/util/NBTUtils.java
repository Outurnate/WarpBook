package panicnot42.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils
{
  public static HashMap<String, INBTSerializable> readHashMapFromNBT(NBTTagCompound tag, Class<INBTSerializable> clazz)
  {
    HashMap<String, INBTSerializable> map = new HashMap<String, INBTSerializable>();
    for (NBTTagCompound e : (Set<NBTTagCompound>)tag.func_150296_c())
    {
      INBTSerializable obj;
      try
      {
        obj = clazz.newInstance();
      }
      catch (InstantiationException e1)
      {
        throw new RuntimeException(e1);
      }
      catch (IllegalAccessException e1)
      {
        throw new RuntimeException(e1);
      }
      obj.readFromNBT(e.getCompoundTag("value"));
      map.put(e.getString("name"), obj);
    }
    return map;
  }
  
  public static void writeHashMapToNBT(NBTTagCompound tag, HashMap<String, INBTSerializable> map)
  {
    int i = 0;
    for (Entry<String, INBTSerializable> e : map.entrySet())
    {
      NBTTagCompound comp = new NBTTagCompound();
      comp.setString("name", e.getKey());
      NBTTagCompound value = new NBTTagCompound();
      e.getValue().writeToNBT(value);
      comp.setTag("value", value);
      tag.setTag(String.format("tag%d", i++), comp);
    }
  }
}
