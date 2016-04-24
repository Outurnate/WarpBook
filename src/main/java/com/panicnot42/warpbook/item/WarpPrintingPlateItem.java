package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class WarpPrintingPlateItem extends Item
{
  public WarpPrintingPlateItem()
  {
    super.setMaxStackSize(64).setUnlocalizedName("warpplate");
  }

  public static ItemStack print(ItemStack book)
  {
    ItemStack stack = new ItemStack(WarpBookMod.items.warpPrintingPlateItem, 1);
    stack.setTagCompound(book.getTagCompound());
    return stack;
  }
}
