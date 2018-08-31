package com.panicnot42.warpbook.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryWarpBook implements IInventory {
	private String name = "warpbook.name";
	
	private final ItemStack stack;
	private final NonNullList<ItemStack> inventoryContents;
	public final int slotsCount = 54;
	
	public InventoryWarpBook(ItemStack heldItem) {
		this.stack = heldItem;
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		
		this.inventoryContents = NonNullList.<ItemStack>withSize(slotsCount, ItemStack.EMPTY);
		
		NBTTagList items = stack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, new ItemStack(item));
			}
		}
	}
	
	@Override
	public int getSizeInventory() {
		return slotsCount;
	}
	
	/**
	 * Returns the stack in the given slot.
	 */
	public ItemStack getStackInSlot(int index) {
		return index >= 0 && index < inventoryContents.size() ? (ItemStack)inventoryContents.get(index) : ItemStack.EMPTY;
	}
	
	
	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
		
		if (!itemstack.isEmpty()) {
			this.markDirty();
		}
		
		return itemstack;
	}
	
	
	/**  Removes a stack from the given slot and returns it. */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemstack = this.inventoryContents.get(index);
		
		if (itemstack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		else {
			this.inventoryContents.set(index, ItemStack.EMPTY);
			return itemstack;
		}
	}
	
	/** Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections). */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventoryContents.set(index, stack);
		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		this.markDirty();
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer playerIn) {
	}
	
	@Override
	public void closeInventory(EntityPlayer playerIn) {
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return SlotWarpBook.itemValid(itemstack);
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
	public void markDirty() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i).getCount() == 0) {
				inventoryContents.set(i, ItemStack.EMPTY);
			}
		}
		
		NBTTagList items = new NBTTagList();
		
		for (int i = 0; i < getSizeInventory(); i++) {
			if (getStackInSlot(i) != ItemStack.EMPTY) {
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				getStackInSlot(i).writeToNBT(item);
				items.appendTag(item);
			}
		}
		
		stack.getTagCompound().setTag("WarpPages", items);
	}
	
	@Override
	public String getName() {
		return I18n.format(name);
	}
	
	@Override
	public boolean hasCustomName() {
		return true;
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return null;
	}
	
	public boolean isEmpty() {
		for (ItemStack itemstack : this.inventoryContents) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		
		return true;
	}


}
