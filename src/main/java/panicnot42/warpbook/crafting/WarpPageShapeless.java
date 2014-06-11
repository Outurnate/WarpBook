package panicnot42.warpbook.crafting;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import panicnot42.warpbook.item.WarpPageItem;

public class WarpPageShapeless extends ShapelessRecipes
{
  ItemStack recipeOutput;

  public WarpPageShapeless(ItemStack recipeOutput, @SuppressWarnings("rawtypes") List recipeItems)
  {
    super(recipeOutput, recipeItems);
    this.recipeOutput = recipeOutput;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventory)
  {
    ItemStack output = recipeOutput.copy();
    try
    {
      for (int i = 0; i < inventory.getSizeInventory(); ++i)
        if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() instanceof WarpPageItem && inventory.getStackInSlot(i).getItemDamage() == 1)
        {
          output.setTagCompound(inventory.getStackInSlot(i).getTagCompound());
        }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return output;
  }
}
