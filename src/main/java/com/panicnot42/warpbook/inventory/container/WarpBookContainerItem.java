package com.panicnot42.warpbook.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.WarpBookDeathlySlot;
import com.panicnot42.warpbook.inventory.WarpBookEnderSlot;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.WarpBookInventorySlot;
import com.panicnot42.warpbook.inventory.WarpBookSlot;

public class WarpBookContainerItem extends Container
{
  public final WarpBookInventoryItem inventory;

  public WarpBookContainerItem(EntityPlayer player, InventoryPlayer inventoryPlayer, WarpBookInventoryItem inventoryItem, WarpBookSpecialInventory inventorySpecial)
  {
    inventory = inventoryItem;
    for (int i = 0; i < WarpBookInventoryItem.INV_SIZE; ++i)
      this.addSlotToContainer(new WarpBookSlot(inventory, i, 8 + (18 * (i % 9)), 18 + (18 * (i / 9))));

    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 9; ++j)
        this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));

    for (int i = 0; i < 9; ++i)
      this.addSlotToContainer(new WarpBookInventorySlot(inventoryPlayer, i, 8 + i * 18, 198));

    if (WarpBookMod.fuelEnabled)
      this.addSlotToContainer(new WarpBookEnderSlot(inventorySpecial, 0, 174, 54));
    if (WarpBookMod.deathPagesEnabled)
      this.addSlotToContainer(new WarpBookDeathlySlot(inventorySpecial, 1, 174, 72));
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

      if (0 <= slotNum && slotNum <= 53) // moving from book
      {
        if (!this.mergeItemStack(moving, 54, 89, true)) // to inv
          return null;

        slot.onSlotChange(moving, itemstack);
      }

      else if (WarpBookSlot.itemValid(slot.getStack()) && !this.mergeItemStack(moving, 0, 54, false)) // moving
                                                                                                      // from
                                                                                                      // inv
                                                                                                      // to
                                                                                                      // book
        return null;

      if (moving.getCount() == 0)
        slot.putStack((ItemStack)null);
      else
        slot.onSlotChanged();

      if (moving.getCount() == itemstack.getCount()) { return null; }

      slot.onTake(player, moving);
    }

    return itemstack;
  }
}
