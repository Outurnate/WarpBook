package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;
import com.panicnot42.warpbook.item.WarpPageItem;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WarpBookDeathlySlot extends Slot
{
  public WarpBookDeathlySlot(WarpBookSpecialInventory inventorySpecial, int i, int j, int k)
  {
    super(inventorySpecial, i, j, k);
  }

  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof WarpPageItem && itemStack.getItemDamage() == 3;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
