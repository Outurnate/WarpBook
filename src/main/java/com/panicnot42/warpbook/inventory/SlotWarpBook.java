package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.item.WarpItem;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWarpBook extends Slot {
	public SlotWarpBook(InventoryWarpBook inventory, int i, int j, int k) {
		super(inventory, i, j, k);
	}
	
	public static boolean itemValid(ItemStack itemStack) {
		return itemStack.getItem() instanceof IDeclareWarp && ((WarpItem)itemStack.getItem()).canGoInBook();
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return itemValid(itemStack);
	}
	
}
