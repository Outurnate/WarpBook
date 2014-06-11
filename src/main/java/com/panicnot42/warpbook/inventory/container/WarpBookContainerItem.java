package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.WarpBookInventorySlot;
import com.panicnot42.warpbook.inventory.WarpBookSlot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WarpBookContainerItem extends Container
{
  public final WarpBookInventoryItem inventory;

  public WarpBookContainerItem(EntityPlayer player, InventoryPlayer inventoryPlayer, WarpBookInventoryItem inventoryItem)
  {
    inventory = inventoryItem;
    for (int i = 0; i < WarpBookInventoryItem.INV_SIZE; ++i)
      this.addSlotToContainer(new WarpBookSlot(inventory, i, 8 + (18 * (i % 9)), 18 + (18 * (i / 9))));

    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 9; ++j)
        this.addSlotToContainer(new WarpBookInventorySlot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));

    for (int i = 0; i < 9; ++i)
      this.addSlotToContainer(new WarpBookInventorySlot(inventoryPlayer, i, 8 + i * 18, 198));
  }

  @Override
  public boolean canInteractWith(EntityPlayer entityplayer)
  {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int par2)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(par2);

    if (slot != null && slot.getHasStack())
    {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (par2 < 6 * 9)
      {
        if (!this.mergeItemStack(itemstack1, 6 * 9, this.inventorySlots.size(), true)) { return null; }
      }
      else if (!this.mergeItemStack(itemstack1, 0, 6 * 9, false)) { return null; }

      if (itemstack1.stackSize == 0)
      {
        slot.putStack((ItemStack)null);
      }
      else
      {
        slot.onSlotChanged();
      }
    }

    return itemstack;
  }
}
