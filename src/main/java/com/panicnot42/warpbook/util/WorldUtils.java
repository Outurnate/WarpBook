package com.panicnot42.warpbook.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WorldUtils {
	public static EntityItem dropItemStackInWorld(World worldObj, double x, double y, double z, ItemStack stack) {
		float f = 0.7F;
		float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
		//entityitem.delayBeforeCanPickup = 10;
		if (stack.hasTagCompound()) {
			entityitem.getItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
		}
		worldObj.spawnEntity(entityitem);
		return entityitem;
	}
}
