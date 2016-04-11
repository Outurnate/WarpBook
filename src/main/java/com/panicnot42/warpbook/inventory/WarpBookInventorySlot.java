package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class WarpBookInventorySlot extends Slot
{
  public WarpBookInventorySlot(InventoryPlayer inventory, int i, int j, int k)
  {
    super(inventory, i, j, k);
  }

  @Override
  public boolean canTakeStack(EntityPlayer par1EntityPlayer)
  {
    return getHasStack() && !(this.getStack().getItem() instanceof WarpBookItem);
  }
}
