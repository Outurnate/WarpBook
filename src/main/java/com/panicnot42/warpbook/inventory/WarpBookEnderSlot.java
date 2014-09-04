package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

public class WarpBookEnderSlot extends Slot
{
  public WarpBookEnderSlot(WarpBookSpecialInventory inventorySpecial, int i, int j, int k)
  {
    super(inventorySpecial, i, j, k);
  }
  
  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof ItemEnderPearl;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
