package com.panicnot42.warpbook;

import java.util.Arrays;

import com.panicnot42.warpbook.crafting.WarpBookShapeless;
import com.panicnot42.warpbook.crafting.WarpPageShapeless;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Crafting
{
  public static void RegisterRecipes()
  {
    RecipeSorter.register("warpbook:shapeless_book", WarpBookShapeless.class, Category.SHAPELESS, "after:minecraft:shapeless");
    RecipeSorter.register("warpbook:shapeless_page", WarpPageShapeless.class, Category.SHAPELESS, "after:minecraft:shapeless");
    
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1),
                                    new ItemStack(Items.ender_pearl, 1),
                                    new ItemStack(Items.paper, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.warpBookItem, 1),
                                    new ItemStack(Items.ender_pearl, 1),
                                    new ItemStack(Items.book, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.potatoWarpPageItem, 1),
                                    new ItemStack(Items.potato, 1),
                                    new ItemStack(Items.paper, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.deathlyWarpPageItem, 1),
                                    new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1),
                                    new ItemStack(Items.iron_ingot, 1));

    GameRegistry.addRecipe(new ItemStack(WarpBookMod.blocks.bookCloner, 1),
                           "isi",
                           "sws",
                           "isi",
                           'i', new ItemStack(Items.iron_ingot, 1),
                           's', new ItemStack(Blocks.bookshelf, 1),
                           'w', new ItemStack(WarpBookMod.items.warpBookItem, 1));
    GameRegistry.addRecipe(new ItemStack(WarpBookMod.blocks.teleporter, 1),
                           "bsb",
                           "coc",
                           "bsb",
                           'b', new ItemStack(Blocks.iron_block, 1),
                           's', new ItemStack(Blocks.bookshelf, 1),
                           'c', new ItemStack(WarpBookMod.blocks.bookCloner, 1),
                           'o', new ItemStack(WarpBookMod.items.warpBookItem, 1));
    
    GameRegistry.addRecipe(new WarpPageShapeless(new ItemStack(WarpBookMod.items.boundWarpPageItem, 2),
                                                 Arrays.asList(new ItemStack(WarpBookMod.items.boundWarpPageItem, 1),
                                                               new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1))));
    GameRegistry.addRecipe(new WarpBookShapeless(new ItemStack(WarpBookMod.items.warpBookItem, 1),
                                                 Arrays.asList(new ItemStack(WarpBookMod.items.warpBookItem, 1),
                                                               new ItemStack(WarpBookMod.items.warpPrintingPlateItem, 1))));
  }
}
