package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.UnboundWarpPageItem;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BlankPageSlot extends Slot
{
  public BlankPageSlot(BookClonerInventoryItem inventory, int i, int j, int k)
  {
    super(inventory, i, j, k);
  }

  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof UnboundWarpPageItem;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
