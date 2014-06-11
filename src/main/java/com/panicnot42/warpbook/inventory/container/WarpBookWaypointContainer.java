package com.panicnot42.warpbook.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

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
