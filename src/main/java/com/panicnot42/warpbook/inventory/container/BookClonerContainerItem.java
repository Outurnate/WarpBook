package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.inventory.BookClonerInventoryItem;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class BookClonerContainerItem extends Container
{
  public final BookClonerInventoryItem inventory;

  public BookClonerContainerItem(EntityPlayer player, InventoryPlayer inventoryPlayer, BookClonerInventoryItem inventoryItem)
  {
    inventory = inventoryItem;
    
    this.addSlotToContainer(new Slot(inventoryItem, 1, 26, 56));
    this.addSlotToContainer(new Slot(inventoryItem, 0, 26, 78));
    this.addSlotToContainer(new Slot(inventoryItem, 2, 133, 56));
    
    for (int i = 0; i < 3; ++i)
      for (int j = 0; j < 9; ++j)
        this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
    
    for (int i = 0; i < 9; ++i)
      this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 198));

  }

  @Override
  public boolean canInteractWith(EntityPlayer entityplayer)
  {
    return true;
  }

  @Override
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
  {
    ItemStack itemstack = null;
    Slot slot = (Slot)this.inventorySlots.get(index);
    
    if (slot != null && slot.getHasStack())
    {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      
      if (index == 2)
      {
        if (!this.mergeItemStack(itemstack1, 3, 39, true))
        {
          return null;
        }
        
        slot.onSlotChange(itemstack1, itemstack);
      }
      else if (index != 1 && index != 0)
      {
        if (itemstack1.getItem() instanceof UnboundWarpPageItem)
        {
          if (!this.mergeItemStack(itemstack1, 0, 1, false))
          {
            return null;
          }
        }
        else if (itemstack1.getItem() instanceof WarpBookItem)
        {
          if (!this.mergeItemStack(itemstack1, 1, 2, false))
          {
            return null;
          }
        }
        else if (index >= 3 && index < 30)
        {
          if (!this.mergeItemStack(itemstack1, 30, 39, false))
          {
            return null;
          }
        }
        else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
        {
          return null;
        }
      }
      else if (!this.mergeItemStack(itemstack1, 3, 39, false))
      {
        return null;
      }
      
      if (itemstack1.stackSize == 0)
      {
        slot.putStack((ItemStack)null);
      }
      else
      {
        slot.onSlotChanged();
      }
      
      if (itemstack1.stackSize == itemstack.stackSize)
      {
        return null;
      }
      
      slot.onPickupFromSlot(playerIn, itemstack1);
    }
    
    return itemstack;
  }
}
