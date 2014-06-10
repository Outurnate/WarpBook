package com.panicnot42.warpbook.inventory.container;


public class WarpBookWaypointContainer extends Container
{
  public ItemStack heldItem;

  public WarpBookWaypointContainer(ItemStack heldItem)
  {
    this.heldItem = heldItem;
  }

  @Override
  public boolean canInteractWith(EntityPlayer entityplayer)
  {
    return true;
  }
}
