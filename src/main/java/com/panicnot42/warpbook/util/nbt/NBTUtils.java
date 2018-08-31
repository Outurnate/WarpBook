package com.panicnot42.warpbook.util.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtils {
	public static <T extends INBTSerializable> HashMap<String, T> readHashMapFromNBT(NBTTagList tag, Class<T> clazz) {
		HashMap<String, T> map = new HashMap<String, T>();
		for (int i = 0; i < tag.tagCount(); ++i) {
			T obj;
			try {
				obj = clazz.newInstance();
			}
			catch (InstantiationException e1) {
				throw new RuntimeException(e1);
			}
			catch (IllegalAccessException e1) {
				throw new RuntimeException(e1);
			}
			obj.readFromNBT(tag.getCompoundTagAt(i).getCompoundTag("value"));
			map.put(tag.getCompoundTagAt(i).getString("name"), obj);
		}
		return map;
	}
	
	public static <T extends INBTSerializable> void writeHashMapToNBT(NBTTagList tag, HashMap<String, T> map) {
		for (Entry<String, T> e : map.entrySet()) {
			NBTTagCompound comp = new NBTTagCompound();
			NBTTagCompound value = new NBTTagCompound();
			e.getValue().writeToNBT(value);
			comp.setString("name", e.getKey());
			comp.setTag("value", value);
			tag.appendTag(comp);
		}
	}
	
	public static <T extends INBTSerializable> ArrayList<T> readArrayListFromNBT(NBTTagList tag, Class<T> clazz) {
		ArrayList<T> array = new ArrayList<T>();
		for (int i = 0; i < tag.tagCount(); ++i) {
			INBTSerializable obj;
			try {
				obj = clazz.newInstance();
			}
			catch (InstantiationException e1) {
				throw new RuntimeException(e1);
			}
			catch (IllegalAccessException e1) {
				throw new RuntimeException(e1);
			}
			obj.readFromNBT(tag.getCompoundTagAt(i));
		}
		return array;
	}
	
	public static <T extends INBTSerializable> void writeArrayListToNBT(NBTTagList tag, ArrayList<T> array) {
		for (T e : array) {
			NBTTagCompound compTag = new NBTTagCompound();
			e.writeToNBT(compTag);
			tag.appendTag(compTag);
		}
	}
	
}
