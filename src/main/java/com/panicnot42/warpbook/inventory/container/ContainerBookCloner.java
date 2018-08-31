package com.panicnot42.warpbook.inventory.container;

import com.panicnot42.warpbook.inventory.InventoryBookCloner;
import com.panicnot42.warpbook.inventory.InventoryBookCloner.InvSlots;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBookCloner extends Container {
	public final InventoryBookCloner inventory;
	public final static int playerInvStart = InvSlots.values().length;
	public final static int playerInvSize = 9 * 4;//Size of entire player's inventory.  4 rows of 9 slots
	
	public ContainerBookCloner(EntityPlayer player, InventoryPlayer inventoryPlayer, InventoryBookCloner inventoryItem) {
		inventory = inventoryItem;

		//Template Book(Slot 0)
		this.addSlotToContainer(new Slot(inventoryItem, InvSlots.TEMPLATE.getIndex(), 16, 48) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof WarpBookItem) && (WarpBookItem.getCopyCost(stack) != 0);
			}
		});
		
		//Blank Pages(Slot 1)
		this.addSlotToContainer(new Slot(inventoryItem, InvSlots.PAGES.getIndex(), 80, 24) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof UnboundWarpPageItem;
			}
		});

		//Empty Book(Slot 2)
		this.addSlotToContainer(new Slot(inventoryItem, InvSlots.COVER.getIndex(), 80, 72) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return (stack.getItem() instanceof WarpBookItem) && (WarpBookItem.getCopyCost(stack) == 0);
			}
		});
		
		//Resulting(Slot 3)
		this.addSlotToContainer(new Slot(inventoryItem, InvSlots.RESULT.getIndex(), 144, 48) {
			
			@Override
			public boolean isItemValid(ItemStack stack) {
				return true;
			}
			
			@Override
			public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
				((InventoryBookCloner) inventory).consumeMaterials();
				return super.onTake(thePlayer, stack);
			}
		});
		
		//Player Inventory Slot 4+ 
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 98 + i * 18));
			}
		}
		
		//Player hotbar
		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 156));
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	protected boolean mergeItemStack(ItemStack stack, int index) {
		return super.mergeItemStack(stack, index, index + 1, false);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack startingItemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(index);
				
		if (slot != null && slot.getHasStack()) {
			ItemStack clickedItemStack = slot.getStack();
			startingItemStack = clickedItemStack.copy();//Make a copy for comparison later
			
			//Handle move from result to the player inventory
			if (index == InvSlots.RESULT.getIndex()) {
				if (!this.mergeItemStack(clickedItemStack, playerInvStart, playerInvStart + playerInvSize, true)) {
					return ItemStack.EMPTY;
				}
				
				slot.onSlotChange(clickedItemStack, startingItemStack);
			}
			//Handle player inventory to the proper cloner slot
			else if (index >= playerInvStart) {
				//Template Book
				if (clickedItemStack.getItem() instanceof UnboundWarpPageItem) {
					if (!this.mergeItemStack(clickedItemStack, InvSlots.PAGES.getIndex())) {
						return ItemStack.EMPTY;
					}
				}
				//Move from player inventory to template slot
				else if ( (clickedItemStack.getItem() instanceof WarpBookItem) && (WarpBookItem.getCopyCost(clickedItemStack) != 0) ) {
					if (!this.mergeItemStack(clickedItemStack, InvSlots.TEMPLATE.getIndex())) {
						return ItemStack.EMPTY;
					}
				}
				//Move from player inventory to cover slot
				else if ( (clickedItemStack.getItem() instanceof WarpBookItem) && (WarpBookItem.getCopyCost(clickedItemStack) == 0) ) {
					if (!this.mergeItemStack(clickedItemStack, InvSlots.COVER.getIndex())) {
						return ItemStack.EMPTY;
					}
				}
				//Move from player inventory to hotbar
				else if (index >= playerInvStart && index < playerInvStart + (9 * 3) ) {
					if (!this.mergeItemStack(clickedItemStack, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				}
				//Move from hotbar to player inventory
				else if ((index >= playerInvStart + (9 * 3) + 1) && (index < playerInvStart + playerInvSize) && !this.mergeItemStack(clickedItemStack, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			}
			//All else failed. Just move from slot to player inventory anywhere
			else if (!this.mergeItemStack(clickedItemStack, playerInvStart, playerInvStart + playerInvSize, false)) {
				return ItemStack.EMPTY;
			}
			
			if (clickedItemStack.isEmpty()) {//The clicked slot ended up empty.
				slot.putStack(ItemStack.EMPTY);//Set it to explicitly empty
			}
			else {//The clicked slot changed count but didn't go empty
				slot.onSlotChanged();
			}
			
			if (clickedItemStack.getCount() == startingItemStack.getCount()) {//Nothing happened
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, clickedItemStack);
		}

		return startingItemStack;
	}
}
