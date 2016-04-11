package com.panicnot42.warpbook.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import net.minecraft.util.IChatComponent;

public class WarpBookSpecialInventory implements IInventory
{
  ItemStack fuel, deathly, heldItem;

  public WarpBookSpecialInventory(ItemStack heldItem)
  {
    int deaths = WarpBookItem.getRespawnsLeft(heldItem), damage = WarpBookItem.getFuelLeft(heldItem);
    fuel = damage == 0 ? null : new ItemStack(Items.ender_pearl, damage);
    deathly = deaths == 0 ? null : new ItemStack(WarpBookMod.unboundWarpPageItem, deaths);
    if (deathly != null) deathly.setItemDamage(3);
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
      if (stack.stackSize > quantity)
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
    if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) itemStack.stackSize = getInventoryStackLimit();
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
    WarpBookItem.setFuelLeft(heldItem, fuel == null ? 0 : fuel.stackSize);
    WarpBookItem.setRespawnsLeft(heldItem, deathly == null ? 0 : deathly.stackSize);
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player)
  {
    return true;
  }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack itemStack)
  {
    return slot == 0 ? itemStack.getItem() instanceof ItemEnderPearl : itemStack.getItem() instanceof WarpPageItem && itemStack.getItemDamage() == 3;
  }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
