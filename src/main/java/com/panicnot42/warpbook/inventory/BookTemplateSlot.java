package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BookTemplateSlot extends Slot
{
  public BookTemplateSlot(BookClonerInventoryItem inventory, int i, int j, int k)
  {
    super(inventory, i, j, k);
  }

  public static boolean itemValid(ItemStack itemStack)
  {
    return itemStack.getItem() instanceof WarpBookItem;
  }

  @Override
  public boolean isItemValid(ItemStack itemStack)
  {
    return itemValid(itemStack);
  }
}
