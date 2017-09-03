package com.panicnot42.warpbook.crafting;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import com.google.gson.JsonObject;
import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.item.WarpPrintingPlateItem;
import com.panicnot42.warpbook.util.CraftingUtils;

public class WarpBookShapeless extends ShapelessRecipes
{
  ItemStack recipeOutput;

  public WarpBookShapeless(String group, ItemStack recipeOutput, NonNullList<Ingredient> ingredients)
  {
    super(group, recipeOutput, ingredients);
    this.recipeOutput = recipeOutput;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventory)
  {
    ItemStack output = recipeOutput.copy();
    try
    {
      for (int i = 0; i < inventory.getSizeInventory(); ++i)
        if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() instanceof WarpPrintingPlateItem)
          output.setTagCompound(inventory.getStackInSlot(i).getTagCompound());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return output;
  }
  
  public static class Factory implements IRecipeFactory
  {
    @Override
    public IRecipe parse(final JsonContext context, final JsonObject json)
    {
      final String group = JsonUtils.getString(json, "group", "");
      final NonNullList<Ingredient> ingredients = CraftingUtils.parseShapeless(context, json);
      final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

      return new WarpBookShapeless(group, result, ingredients);
    }
  }
}
