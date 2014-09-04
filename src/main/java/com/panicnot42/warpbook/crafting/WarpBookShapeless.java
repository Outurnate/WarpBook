package com.panicnot42.warpbook.crafting;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class WarpBookShapeless extends ShapelessRecipes
{
  ItemStack recipeOutput;

  public WarpBookShapeless(ItemStack recipeOutput, @SuppressWarnings("rawtypes") List recipeItems)
  {
    super(recipeOutput, recipeItems);
    this.recipeOutput = recipeOutput;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventory)
  {
    return new ItemStack(WarpBookMod.warpBookItem, 1, 16);
  }
}
