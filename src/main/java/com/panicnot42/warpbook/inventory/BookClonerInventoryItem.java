package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class BookClonerInventoryItem implements IInventory
{
  private String name = "bookcloner.name";

  private final TileEntityBookCloner cloner;

  public static final int INV_SIZE = 3;

  ItemStack[] inventory = new ItemStack[INV_SIZE];

  public BookClonerInventoryItem(TileEntityBookCloner cloner)
  {
    this.cloner = cloner;
    inventory[0] = cloner.getBooks();
    inventory[1] = cloner.getPages();
    inventory[2] = cloner.getResult();
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
    /*switch(i)
    {
    case 0:
      return itemstack.getItem() instanceof UnboundWarpPageItem;
    case 1:
      return itemstack.getItem() instanceof WarpBookItem && !itemstack.hasTagCompound();
    }
    return false;*/
    return true;
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
    cloner.setBooks(inventory[0]);
    cloner.setPages(inventory[1]);
    cloner.setResult(inventory[2]);
    if (cloner.performOperation())
    {
      inventory[0] = cloner.getBooks();
      inventory[1] = cloner.getPages();
      inventory[2] = cloner.getResult();
    }
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
  public IChatComponent getDisplayName()
  {
    return null;
  }
}
