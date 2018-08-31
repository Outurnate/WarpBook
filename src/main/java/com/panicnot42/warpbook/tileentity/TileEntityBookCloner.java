package com.panicnot42.warpbook.tileentity;

import com.panicnot42.warpbook.inventory.InventoryBookCloner.InvSlots;
import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBookCloner extends TileEntity {
	private ItemStack pages;
	private ItemStack template;
	private ItemStack result;
	private ItemStack cover;
	
	public TileEntityBookCloner() {
		pages = ItemStack.EMPTY;
		template = ItemStack.EMPTY;
		result = ItemStack.EMPTY;
		cover = ItemStack.EMPTY;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		pages = new ItemStack(tag.getCompoundTag("pages"));
		template = new ItemStack(tag.getCompoundTag("books"));
		result = new ItemStack(tag.getCompoundTag("result"));
		cover = new ItemStack(tag.getCompoundTag("cover"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (!pages.isEmpty()) {
			tag.setTag("pages", pages.writeToNBT(new NBTTagCompound()));
		}
		if (!template.isEmpty()) {
			tag.setTag("books", template.writeToNBT(new NBTTagCompound()));
		}
		if (!result.isEmpty()) {
			tag.setTag("result", result.writeToNBT(new NBTTagCompound()));
		}
		if (!cover.isEmpty()) {
			tag.setTag("cover", cover.writeToNBT(new NBTTagCompound()));
		}

		return tag;
	}
	
	public ItemStack getPages() {
		return pages;
	}
	
	public ItemStack getTemplate() {
		return template;
	}
	
	public ItemStack getResult() {
		return result;
	}
	
	public ItemStack getCover() {
		return cover;
	}
	
	public void setItem(InvSlots invSlot, ItemStack stack) {
		switch(invSlot) {
			default:
			case COVER: setCover(stack); break;
			case PAGES: setPages(stack); break;
			case RESULT: setResult(stack); break;
			case TEMPLATE: setTemplate(stack); break;
		}
	}
	
	public void setCover(ItemStack cover) {
		this.cover = cover;
		this.markDirty();
	}
	
	public void setPages(ItemStack pages) {
		this.pages = pages;
		this.markDirty();
	}
	
	public void setTemplate(ItemStack books) {
		this.template = books;
		this.markDirty();
	}
	
	public void setResult(ItemStack result) {
		this.result = result;
		this.markDirty();
	}
	
	public boolean materialsReady() {
		if (!pages.isEmpty() && !template.isEmpty() && !cover.isEmpty()) {
			if (pages.getCount() >= WarpBookItem.getCopyCost(template)) {
				return true;
			}
		}
		return false;
	}
	
	public void buildBook(World world) {
		setResult(WarpBookItem.copyBook(world, template));
		markDirty();
	}
	
	public void consumeMaterials() {
		int cost = WarpBookItem.getCopyCost(template);
		pages.shrink(cost);
		if (pages.isEmpty()) {
			pages = ItemStack.EMPTY;
		}
		cover.shrink(1);
		if (cover.isEmpty()) {
			cover = ItemStack.EMPTY;
		}
	}
	
}
