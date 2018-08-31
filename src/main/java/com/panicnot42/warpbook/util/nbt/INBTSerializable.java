package com.panicnot42.warpbook.util.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSerializable {
	public abstract void readFromNBT(NBTTagCompound var1);
	
	public abstract void writeToNBT(NBTTagCompound var1);
}
