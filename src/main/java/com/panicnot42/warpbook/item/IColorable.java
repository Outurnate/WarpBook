package com.panicnot42.warpbook.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorable {

	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int tintIndex);
	
}
