package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.item.Item;

public class WarpFuelItem extends Item
{
  public WarpFuelItem(String name)
  {
    super.setMaxStackSize(64).setCreativeTab(WarpBookMod.tabBook).setUnlocalizedName(name);
  }
}
