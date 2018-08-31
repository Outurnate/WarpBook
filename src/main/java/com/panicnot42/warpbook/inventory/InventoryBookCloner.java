package com.panicnot42.warpbook.inventory;

import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class InventoryBookCloner implements IInventory {
	private String name = "bookcloner.name";
	private final TileEntityBookCloner cloner;
	public static final int INV_SIZE = 4;
	ItemStack[] inventory = new ItemStack[INV_SIZE];
	
	public enum InvSlots {
		TEMPLATE(0),
		PAGES(1),
		COVER(2),
		RESULT(3);
		
		private int index;
		
		private InvSlots(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}
	
	public InventoryBookCloner(TileEntityBookCloner cloner) {		
		this.cloner = cloner;
		inventory[InvSlots.TEMPLATE.getIndex()] = cloner.getTemplate();
		inventory[InvSlots.PAGES.getIndex()] = cloner.getPages();
		inventory[InvSlots.COVER.getIndex()] = cloner.getCover();
		inventory[InvSlots.RESULT.getIndex()] = cloner.getResult();
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int quantity) {
		ItemStack stack = getStackInSlot(slot);
		if (!stack.isEmpty()) {
			if (stack.getCount() > quantity) {
				stack = stack.splitStack(quantity);
			}
			else {
				setInventorySlotContents(slot, ItemStack.EMPTY);
			}
			markDirty();
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
		inventory[slot] = itemStack;
		if (!itemStack.isEmpty() && itemStack.getCount() > getInventoryStackLimit()) {
			itemStack.setCount(getInventoryStackLimit());
		}
		markDirty();
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
				
		cloner.setTemplate(inventory[InvSlots.TEMPLATE.getIndex()]);
		cloner.setPages(inventory[InvSlots.PAGES.getIndex()]);
		cloner.setCover(inventory[InvSlots.COVER.getIndex()]);
		cloner.setResult(inventory[InvSlots.RESULT.getIndex()]);
		
		if(cloner.materialsReady()) {
			cloner.buildBook(cloner.getWorld());
			inventory[InvSlots.RESULT.getIndex()] = cloner.getResult();
		} else {
			cloner.setResult(ItemStack.EMPTY);
			inventory[InvSlots.RESULT.getIndex()] = ItemStack.EMPTY;
		}
		
		/*cloner.setResult(inventory[InvSlots.RESULT.getIndex()]);
		if (cloner.performOperation()) {
			inventory[InvSlots.TEMPLATE.getIndex()] = cloner.getTemplate();
			inventory[InvSlots.PAGES.getIndex()] = cloner.getPages();
			inventory[InvSlots.COVER.getIndex()] = cloner.getCover();
			inventory[InvSlots.RESULT.getIndex()] = cloner.getResult();
		}*/
	}
	
	public void consumeMaterials() {
		cloner.consumeMaterials();
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

	@Override
	public boolean isEmpty() {
		return getSizeInventory() == 0;
	}
	
}
