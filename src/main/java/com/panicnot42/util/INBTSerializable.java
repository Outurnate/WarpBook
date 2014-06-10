package com.panicnot42.util;


public interface INBTSerializable
{
  public abstract void readFromNBT(NBTTagCompound var1);

  public abstract void writeToNBT(NBTTagCompound var1);
}
