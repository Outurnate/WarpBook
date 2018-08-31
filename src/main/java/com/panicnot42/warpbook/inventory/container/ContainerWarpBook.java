package com.panicnot42.warpbook.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.SlotWarpBookDeathly;
//import com.panicnot42.warpbook.inventory.SlotWarpBookEnder;
import com.panicnot42.warpbook.inventory.InventoryWarpBook;
import com.panicnot42.warpbook.inventory.SlotWarpBookInventory;
import com.panicnot42.warpbook.inventory.SlotWarpBook;

public class ContainerWarpBook extends Container {
	public final InventoryWarpBook inventory;
	
	public ContainerWarpBook(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryWarpBook inventoryItem, InventoryWarpBookSpecial inventorySpecial) {
		inventory = inventoryItem;
		for (int i = 0; i < inventoryItem.getSizeInventory(); i++) {
			this.addSlotToContainer(new SlotWarpBook(inventory, i, 8 + (18 * (i % 9)), 18 + (18 * (i / 9))));
		}
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new SlotWarpBookInventory(inventoryPlayer, i, 8 + i * 18, 198));
		}
		
		if (WarpBookMod.deathPagesEnabled) {
			this.addSlotToContainer(new SlotWarpBookDeathly(inventorySpecial, 0, 174, 72));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(slotNum);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack moving = slot.getStack();
			itemstack = moving.copy();
			
			if (0 <= slotNum && slotNum <= 53) {// moving from book
			
				if (!this.mergeItemStack(moving, 54, 89, true)) {// to inv
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(moving, itemstack);
			}
			else if (SlotWarpBook.itemValid(slot.getStack()) && !this.mergeItemStack(moving, 0, 54, false)) {// moving from inv to book
				return ItemStack.EMPTY;
			}
			
			if (moving.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (moving.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, moving);
		}
		
		return itemstack;
	}
	
}
