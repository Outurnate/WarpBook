package com.panicnot42.warpbook.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;

public class WarpBookInventoryItem implements IInventory
{
  private String name = "warpbook.name";

  private final ItemStack stack;

  public static final int INV_SIZE = 54;

  ItemStack[] inventory = new ItemStack[INV_SIZE];

  public WarpBookInventoryItem(ItemStack heldItem)
  {
    this.stack = heldItem;
    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    
    NBTTagList items = stack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    for (int i = 0; i < items.tagCount(); ++i)
    {
      NBTTagCompound item = items.getCompoundTagAt(i);
      int slot = item.getInteger("Slot");
      if (slot >= 0 && slot < getSizeInventory())
        setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
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
        setInventorySlotContents(slot, null);
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
    inventory[slot] = itemStack;
    if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
      itemStack.stackSize = getInventoryStackLimit();
    markDirty();
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
  public void openInventory(EntityPlayer playerIn)
  {
  }
  
  @Override
  public void closeInventory(EntityPlayer playerIn)
  {
  }
  
  @Override
  public boolean isItemValidForSlot(int i, ItemStack itemstack)
  {
    return WarpBookSlot.itemValid(itemstack);
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
  public void markDirty()
  {
    for (int i = 0; i < getSizeInventory(); ++i)
      if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
        setInventorySlotContents(i, null);
    
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
  public String getName()
  {
    return I18n.format(name);
  }
  
  @Override
  public boolean hasCustomName()
  {
    return true;
  }
  
  @Override
  public ITextComponent getDisplayName()
  {
    return null;
  }
}
