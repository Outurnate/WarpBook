package com.panicnot42.warpbook.inventory;


public class WarpBookInventorySlot extends Slot
{
  public WarpBookInventorySlot(InventoryPlayer inventory, int i, int j, int k)
  {
    super(inventory, i, j, k);
  }

  @Override
  public boolean canTakeStack(EntityPlayer par1EntityPlayer)
  {
    return getHasStack() && WarpBookSlot.itemValid(getStack());
  }
}
