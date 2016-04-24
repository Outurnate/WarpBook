package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.inventory.BookClonerInventoryItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BookClonerContainerItem extends Container
{
  public final BookClonerInventoryItem inventory;

  public BookClonerContainerItem(EntityPlayer player, InventoryPlayer inventoryPlayer, BookClonerInventoryItem inventoryItem)
  {
    inventory = inventoryItem;
    
    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 9; ++j)
        this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9 + 3, 8 + j * 18, 140 + i * 18));
    
    for (int i = 0; i < 9; ++i)
      this.addSlotToContainer(new Slot(inventoryPlayer, i + 3, 8 + i * 18, 198));

    this.addSlotToContainer(new Slot(inventoryItem, 1, 26, 56));
    this.addSlotToContainer(new Slot(inventoryItem, 0, 26, 78));
    this.addSlotToContainer(new Slot(inventoryItem, 2, 133, 56));
  }

  @Override
  public boolean canInteractWith(EntityPlayer entityplayer)
  {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(slotNum);

    if (slot != null && slot.getHasStack())
    {
      ItemStack moving = slot.getStack();
      itemstack = moving.copy();

      if (0 <= slotNum && slotNum <= 2) // moving from block
      {
        if (!this.mergeItemStack(moving, 3, 38, true)) // to inv
          return null;

        slot.onSlotChange(moving, itemstack);
      }

      else if (!this.mergeItemStack(moving, 0, 2, false))
        return null;

      if (moving.stackSize == 0)
        slot.putStack((ItemStack)null);
      else
        slot.onSlotChanged();

      if (moving.stackSize == itemstack.stackSize)
        return null;

      slot.onPickupFromSlot(player, moving);
    }

    return itemstack;
  }
}
