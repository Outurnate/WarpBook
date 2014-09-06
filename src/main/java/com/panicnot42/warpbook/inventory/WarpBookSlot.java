package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.WarpPageItem;

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
    return itemStack.getItem() instanceof WarpPageItem && itemStack.getItemDamage() != 0 && itemStack.getItemDamage() != 3 && itemStack.getItemDamage() != 4;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
