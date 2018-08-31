package com.panicnot42.warpbook.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.LegacyWarpPageItem;
import net.minecraft.util.text.ITextComponent;

public class InventoryWarpBookSpecial implements IInventory {
	ItemStack deathly, heldItem;
	
	public InventoryWarpBookSpecial(ItemStack heldItem) {
		int deaths = WarpBookItem.getRespawnsLeft(heldItem); 
		deathly = deaths == 0 ? ItemStack.EMPTY : new ItemStack(WarpBookMod.items.deathlyWarpPageItem, deaths);
		this.heldItem = heldItem;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot == 0 ? deathly : ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int quantity) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != ItemStack.EMPTY) {
			if (stack.getCount() > quantity) {
				stack = stack.splitStack(quantity);
				markDirty();
			}
			else {
				setInventorySlotContents(slot, ItemStack.EMPTY);
			}
		}
		return stack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot) {
		ItemStack stack = getStackInSlot(slot);
		setInventorySlotContents(slot, ItemStack.EMPTY);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		if (slot == 0) {
			deathly = itemStack;
		}
		if (itemStack != null && itemStack.getCount() > getInventoryStackLimit()) {
			itemStack.setCount(getInventoryStackLimit());
		}
		markDirty();
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 16;
	}
	
	@Override
	public void markDirty() {
		WarpBookItem.setRespawnsLeft(heldItem, deathly == ItemStack.EMPTY ? 0 : deathly.getCount());
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return slot == 0 ? itemStack.getItem() instanceof ItemEnderPearl : itemStack.getItem() instanceof LegacyWarpPageItem && itemStack.getItemDamage() == 3;
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
	public ITextComponent getDisplayName() {
		return null;
	}
	
	@Override
	public boolean isEmpty() {
		return getSizeInventory() == 0;
	}

}
