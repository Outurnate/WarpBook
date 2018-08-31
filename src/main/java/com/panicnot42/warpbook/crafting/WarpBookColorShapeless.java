package com.panicnot42.warpbook.crafting;

import com.panicnot42.warpbook.item.WarpBookItem;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class WarpBookColorShapeless extends ShapelessRecipes {
	ItemStack recipeOutput;
	
	public WarpBookColorShapeless(ItemStack recipeOutput, NonNullList<Ingredient> ingredients) {
		super("", recipeOutput, ingredients);
		this.recipeOutput = recipeOutput;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		
		ItemStack dye = ItemStack.EMPTY;
		ItemStack book = ItemStack.EMPTY;
		ItemStack water = ItemStack.EMPTY;
		
		try {
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack workingStack = inventory.getStackInSlot(i);
				if(workingStack.getItem() instanceof WarpBookItem) {
					book = workingStack.copy();
				}
				else if(workingStack.getItem() == Items.DYE ) {
					dye = workingStack.copy();
				}
				else if(workingStack.getItem() == Items.WATER_BUCKET) {
					water = workingStack.copy();
				}
			}

			if(!book.isEmpty()) {
				if(!book.hasTagCompound()) {
					book.setTagCompound(new NBTTagCompound());
				}

				if(!dye.isEmpty()) {
					EnumDyeColor dyeColor = EnumDyeColor.byDyeDamage(dye.getItemDamage());
				
					book.getTagCompound().setInteger("color", dyeColor.getColorValue());
				}
				else if(!water.isEmpty()) {
					book.getTagCompound().removeTag("color");
				}

				return book;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ItemStack.EMPTY;
	}
	
}