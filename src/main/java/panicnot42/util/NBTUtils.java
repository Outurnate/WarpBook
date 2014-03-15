package panicnot42.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtils
{
  public static HashMap<String, INBTSerializable> readHashMapFromNBT(NBTTagList tag, Class<INBTSerializable> clazz)
  {
    HashMap<String, INBTSerializable> map = new HashMap<String, INBTSerializable>();
    for (int i = 0; i < tag.tagCount(); ++i)
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
      obj.readFromNBT(tag.getCompoundTagAt(i).getCompoundTag("value"));
      map.put(tag.getCompoundTagAt(i).getString("name"), obj);
    }
    return map;
  }
  
  public static void writeHashMapToNBT(NBTTagList tag, HashMap<String, INBTSerializable> map)
  {
    for (Entry<String, INBTSerializable> e : map.entrySet())
    {
      NBTTagCompound comp = new NBTTagCompound();
      NBTTagCompound value = new NBTTagCompound();
      e.getValue().writeToNBT(value);
      comp.setString("name", e.getKey());
      comp.setTag("value", value);
      tag.appendTag(comp);
    }
  }
}
