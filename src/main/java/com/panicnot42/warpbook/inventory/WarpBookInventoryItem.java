package com.panicnot42.warpbook.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class WarpBookInventoryItem implements IInventory
{
  private String name = "warpbook.name";

  private final ItemStack stack;

  public static final int INV_SIZE = 54;

  ItemStack[] inventory = new ItemStack[INV_SIZE];

  public WarpBookInventoryItem(ItemStack heldItem)
  {
    this.stack = heldItem;
    if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());

    NBTTagList items = stack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    for (int i = 0; i < items.tagCount(); ++i)
    {
      NBTTagCompound item = items.getCompoundTagAt(i);
      int slot = item.getInteger("Slot");
      if (slot >= 0 && slot < getSizeInventory()) setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
    }
  }

  @Override
  public int getSizeInventory()
  {
    return inventory.length;
  }

  @Override
  public ItemStack getStackInSlot(int i)
  {
    return inventory[i];
  }

  @Override
  public ItemStack decrStackSize(int slot, int qty)
  {
    ItemStack stack = getStackInSlot(slot);
    if (stack != null)
    {
      if (stack.stackSize > qty)
      {
        stack = stack.splitStack(qty);
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
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    ItemStack stack = getStackInSlot(slot);
    setInventorySlotContents(slot, null);
    return stack;
  }

  @Override
  public void setInventorySlotContents(int slot, ItemStack itemstack)
  {
    inventory[slot] = itemstack;
    if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) itemstack.stackSize = getInventoryStackLimit();
    markDirty();
  }

  @Override
  public String getInventoryName()
  {
    return I18n.format(name);
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer entityplayer)
  {
    return true;
  }

  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return WarpBookSlot.itemValid(itemstack);
  }

  @Override
  public void openInventory()
  {
  }

  @Override
  public void closeInventory()
  {
  }

  @Override
  public void markDirty()
  {
    for (int i = 0; i < getSizeInventory(); ++i)
    {
      if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) setInventorySlotContents(i, null);
    }

    NBTTagList items = new NBTTagList();

    for (int i = 0; i < getSizeInventory(); ++i)
      if (getStackInSlot(i) != null)
      {
        NBTTagCompound item = new NBTTagCompound();
        item.setInteger("Slot", i);
        getStackInSlot(i).writeToNBT(item);
        items.appendTag(item);
      }
    stack.getTagCompound().setTag("WarpPages", items);
  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }
}
