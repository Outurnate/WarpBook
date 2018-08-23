package com.panicnot42.warpbook.util;

import com.panicnot42.warpbook.util.nbt.INBTSerializable;

import net.minecraft.nbt.NBTTagCompound;

public class Waypoint implements INBTSerializable {
	public String friendlyName, name;
	public int x, y, z, dim;
	
	public Waypoint(String friendlyName, String name, int x, int y, int z, int dim) {
		this.friendlyName = friendlyName;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
	}
	
	public Waypoint(NBTTagCompound var1) {
		readFromNBT(var1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound var1) {
		this.friendlyName = var1.getString("friendlyName");
		this.x = var1.getInteger("x");
		this.y = var1.getInteger("y");
		this.z = var1.getInteger("z");
		this.dim = var1.getInteger("dim");
		this.name = var1.getString("name");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound var1) {
		var1.setString("friendlyName", this.friendlyName);
		var1.setInteger("x", this.x);
		var1.setInteger("y", this.y);
		var1.setInteger("z", this.z);
		var1.setInteger("dim", this.dim);
		var1.setString("name", this.name);
	}

}