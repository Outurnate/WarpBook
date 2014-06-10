package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.WarpPageItem;

public class WarpBookSlot extends Slot
{
  public WarpBookSlot(WarpBookInventoryItem inventory, int i, int j, int k)
  {
    super(inventory, i, j, k);
  }

  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof WarpPageItem && (itemStack.getItemDamage() == 1 || itemStack.getItemDamage() == 2);
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
