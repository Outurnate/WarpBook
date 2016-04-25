package com.panicnot42.warpbook.item;

import net.minecraft.item.Item;

public class WarpFuelItem extends Item
{
  public WarpFuelItem(String name)
  {
    super.setMaxStackSize(64).setUnlocalizedName(name);
  }
}
