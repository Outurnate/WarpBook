package com.panicnot42.warpbook.inventory;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;
import com.panicnot42.warpbook.item.WarpFuelItem;

public class WarpBookEnderSlot extends Slot
{
  public WarpBookEnderSlot(WarpBookSpecialInventory inventorySpecial, int i, int j, int k)
  {
    super(inventorySpecial, i, j, k);
  }

  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof WarpFuelItem;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
