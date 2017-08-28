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
                                    new ItemStack(Items.ENDER_PEARL, 1),
                                    new ItemStack(Items.PAPER, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.warpBookItem, 1),
                                    new ItemStack(Items.ENDER_PEARL, 1),
                                    new ItemStack(Items.BOOK, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.potatoWarpPageItem, 1),
                                    new ItemStack(Items.POTATO, 1),
                                    new ItemStack(Items.PAPER, 1));
    GameRegistry.addShapelessRecipe(new ItemStack(WarpBookMod.items.deathlyWarpPageItem, 1),
                                    new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1),
                                    new ItemStack(Items.IRON_INGOT, 1));

    GameRegistry.addRecipe(new ItemStack(WarpBookMod.blocks.bookCloner, 1),
                           "isi",
                           "sws",
                           "isi",
                           'i', new ItemStack(Items.IRON_INGOT, 1),
                           's', new ItemStack(Blocks.BOOKSHELF, 1),
                           'w', new ItemStack(WarpBookMod.items.warpBookItem, 1));
    GameRegistry.addRecipe(new ItemStack(WarpBookMod.blocks.teleporter, 1),
                           "bsb",
                           "coc",
                           "bsb",
                           'b', new ItemStack(Blocks.IRON_BLOCK, 1),
                           's', new ItemStack(Blocks.BOOKSHELF, 1),
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
