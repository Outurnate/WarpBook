package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.WarpItems;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class WarpBookSpecialInventory implements IInventory
{
  ItemStack fuel, deathly, heldItem;

  public WarpBookSpecialInventory(ItemStack heldItem)
  {
    int deaths = WarpBookItem.getRespawnsLeft(heldItem), damage = WarpBookItem.getFuelLeft(heldItem);
    fuel = damage == 0 ? null : new ItemStack(Items.ENDER_PEARL, damage);
    deathly = deaths == 0 ? null : new ItemStack(WarpItems.deathlyWarpPageItem, deaths);
    this.heldItem = heldItem;
  }

  @Override
  public int getSizeInventory()
  {
    return 2;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return slot == 0 ? fuel : deathly;
  }

  @Override
  public ItemStack decrStackSize(int slot, int quantity)
  {
    ItemStack stack = getStackInSlot(slot);
    if (stack != null)
    {
      if (stack.getCount() > quantity)
      {
        stack = stack.splitStack(quantity);
        markDirty();
      }
      else
      {
        setInventorySlotContents(slot, null);
      }
    }
    return stack;
  }

  @Override
  public ItemStack removeStackFromSlot(int slot)
  {
    ItemStack stack = getStackInSlot(slot);
    setInventorySlotContents(slot, null);
    return stack;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack itemStack)
  {
    if (slot == 0)
      fuel = itemStack;
    else
      deathly = itemStack;
    if (itemStack != null && itemStack.getCount() > getInventoryStackLimit()) itemStack.setCount(getInventoryStackLimit());
    markDirty();
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 16;
  }

  @Override
  public void markDirty()
  {
    WarpBookItem.setFuelLeft(heldItem, fuel == null ? 0 : fuel.getCount());
    WarpBookItem.setRespawnsLeft(heldItem, deathly == null ? 0 : deathly.getCount());
  }

  @Override
  public boolean isUsableByPlayer(EntityPlayer player)
  {
    return true;
  }

  @Override
  public void openInventory(EntityPlayer player)
  {
  }

  @Override
  public void closeInventory(EntityPlayer player)
  {
  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemStack)
  {
    return slot == 0 ? itemStack.getItem() instanceof ItemEnderPearl : itemStack.getItem() instanceof IDeclareWarp;
  }

  @Override
  public int getField(int id)
  {
    return 0;
  }

  @Override
  public void setField(int id, int value)
  {
  }

  @Override
  public int getFieldCount()
  {
    return 0;
  }

  @Override
  public void clear()
  {
  }

  @Override
  public String getName()
  {
    return null;
  }

  @Override
  public boolean hasCustomName()
  {
    return false;
  }

  @Override
  public ITextComponent getDisplayName()
  {
    return null;
  }

  @Override
  public boolean isEmpty()
  {
    return fuel.isEmpty() && deathly.isEmpty() && heldItem.isEmpty();
  }
}
