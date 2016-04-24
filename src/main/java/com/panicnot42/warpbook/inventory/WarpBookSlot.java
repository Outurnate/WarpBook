package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.core.IDeclareWarp;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WarpBookSlot extends Slot
{
  public WarpBookSlot(WarpBookInventoryItem inventory, int i, int j, int k)
  {
    super(inventory, i, j, k);
  }

  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof IDeclareWarp;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
