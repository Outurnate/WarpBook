package com.panicnot42.warpbook;

import com.panicnot42.warpbook.crafting.WarpBookColorShapeless;
import com.panicnot42.warpbook.crafting.WarpPageShapeless;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.item.WarpPotionItem;
import com.panicnot42.warpbook.util.JavaUtils;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.registries.IForgeRegistry;

public class Crafting {
	
	public void register(IForgeRegistry<IRecipe> registry) {
		
		RecipeSorter.register("warpbook:shapeless_page", WarpPageShapeless.class, Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("warpbook:shapeless_dyewarpbook", WarpBookColorShapeless.class, Category.SHAPELESS, "after:minecraft:shapeless");
		
		PotionType awkward = PotionType.REGISTRY.getObject(new ResourceLocation("awkward"));
		
		//Brewing recipe for unbound warp page
		BrewingRecipeRegistry.addRecipe(
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), awkward),
				new ItemStack(WarpBookMod.items.warpClusterItem), //Warp Cluster
				new ItemStack(WarpBookMod.items.unboundWarpPotionItem));
		
		//Recipe for unbound warp book
		GameRegistry.addShapelessRecipe(
			new ResourceLocation(Properties.modid, "unboundWarpPage"),//Name
			null,//Group
			new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1),//Output
			new Ingredient[] {
				Ingredient.fromStacks(new ItemStack(Items.PAPER, 1)),
				Ingredient.fromStacks(new ItemStack(WarpBookMod.items.unboundWarpPotionItem))
			}
		);
		
		//Recipe for warp book
		GameRegistry.addShapelessRecipe(
			new ResourceLocation(Properties.modid, "warpBook"),//Name
			null,//Group
			new ItemStack(WarpBookMod.items.warpBookItem, 1),//Output
			new Ingredient[] {
				Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL, 1)),
				Ingredient.fromStacks(new ItemStack(Items.BOOK, 1))
			}
		);
		
		//Recipe for deathly warp page
		GameRegistry.addShapedRecipe(
			new ResourceLocation(Properties.modid, "deathlyWarpPage"),//Name
			null,//Group
			new ItemStack(WarpBookMod.items.deathlyWarpPageItem),//Output
			" f ",
			"dud",
			'u', new ItemStack(WarpBookMod.items.unboundWarpPageItem),
			'd', new ItemStack(Items.DIAMOND),
			'f', new ItemStack(Items.FERMENTED_SPIDER_EYE)
		);
		
		//Recipe for warp cluster
		GameRegistry.addShapedRecipe(
			new ResourceLocation(Properties.modid, "warpCluster"),//Name
			null,//Group
			new ItemStack(WarpBookMod.items.warpClusterItem),//Output
			"eee",
			"ere",
			"eee",
			'e', new ItemStack(Items.ENDER_PEARL),
			'r', new ItemStack(Items.REDSTONE)
		);
		
		//Recipe for book cloner
		GameRegistry.addShapedRecipe(
			new ResourceLocation(Properties.modid, "bookCloner"),//Name
			null,//Group
			new ItemStack(WarpBookMod.blocks.bookCloner, 1),//Output
			"sss",
			"owo",
			"ggg",
			's', new ItemStack(Blocks.STONE_SLAB, 1, 1),
			'o', new ItemStack(Blocks.OBSIDIAN),
			'w', new ItemStack(WarpBookMod.items.warpClusterItem),
			'g', new ItemStack(Items.GOLD_INGOT)
		);
		
		//Recipe for teleporter
		GameRegistry.addShapedRecipe(
			new ResourceLocation(Properties.modid, "teleporter"),//Name
			null,//Group
			new ItemStack(WarpBookMod.blocks.teleporter, 1),//Output
			"qpq",
			"ece",
			"obo",
			'q', new ItemStack(Items.QUARTZ),
			'p', new ItemStack(Blocks.STONE_PRESSURE_PLATE),
			'e', new ItemStack(Blocks.END_ROD),
			'c', new ItemStack(WarpBookMod.items.warpClusterItem),
			'o', new ItemStack(Blocks.OBSIDIAN),
			'b', new ItemStack(Items.GLASS_BOTTLE)
		);
		
		//Recipe to copy bound warp page to an unbound page
		copyPageToUnboundPage(registry, (WarpPageItem) WarpBookMod.items.locusWarpPageItem);
		
		//Recipe to copy hyper warp page to an unbound page
		copyPageToUnboundPage(registry, (WarpPageItem) WarpBookMod.items.hyperWarpPageItem);

		//Recipe to apply locus potion to an unbound page or plain paper
		potionToPaper(registry, (WarpPotionItem)WarpBookMod.items.locusWarpPotionItem, (WarpPageItem) WarpBookMod.items.locusWarpPageItem);
		
		//Recipe to apply player potion to an unbound page or plain paper
		potionToPaper(registry, (WarpPotionItem)WarpBookMod.items.playerWarpPotionItem, (WarpPageItem) WarpBookMod.items.playerWarpPageItem);
		
		//Recipe to apply hyper potion to an unbound page or plain paper
		potionToPaper(registry, (WarpPotionItem)WarpBookMod.items.hyperWarpPotionItem, (WarpPageItem) WarpBookMod.items.hyperWarpPageItem);
		
		String[] dyeValues = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };
		
		//Recipe to color a warp book cover
		for(EnumDyeColor color : EnumDyeColor.values()) {
			ItemStack dyedBook = new ItemStack(WarpBookMod.items.warpBookItem);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("color", getColorValue(color));
			dyedBook.setTagCompound(tag);
			registry.register(
				new WarpBookColorShapeless(
					dyedBook,//Output
					NonNullList.from(null,
						Ingredient.fromStacks(new ItemStack(WarpBookMod.items.warpBookItem)),
						new OreIngredient("dye" + dyeValues[color.getDyeDamage()])
					)
				).setRegistryName(Properties.modid, "dyeWarpBook_" + color.getDyeDamage())
			);
		}
		
		//Recipe to clear the color from a warp book cover
		ItemStack dyedBook = new ItemStack(WarpBookMod.items.warpBookItem);
		NBTTagCompound tag = new NBTTagCompound();
		//We'll set the washing example to magenta because it's pretty ugly and full of regret. ;)
		tag.setInteger("color", getColorValue(EnumDyeColor.MAGENTA));
		dyedBook.setTagCompound(tag);
		registry.register(
			new WarpBookColorShapeless(
					new ItemStack(WarpBookMod.items.warpBookItem),//Output
				NonNullList.from(null,
					Ingredient.fromStacks(dyedBook),
					Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET))
				)
			).setRegistryName(Properties.modid, "dyeWarpBook_X")
		);
		
	}
	
	public int getColorValue(EnumDyeColor from) {
		return (int) JavaUtils.getRestrictedObject(EnumDyeColor.class, from, "field_193351_w", "colorValue");
	}
	
	private void potionToPaper(IForgeRegistry<IRecipe> registry, WarpPotionItem potionIn, WarpPageItem pageOut) {
		
		if (potionIn.isWarpCloneable(new ItemStack(potionIn))) {
			for(Item paper : new Item[]{Items.PAPER, WarpBookMod.items.unboundWarpPageItem}) {
				String recipeName = upperFirst(pageOut.getRegistryName().getResourcePath()) + 
					"From" + upperFirst(potionIn.getRegistryName().getResourcePath()) + 
					"And" + upperFirst(paper.getRegistryName().getResourcePath());
			
				registry.register(
					new WarpPageShapeless(
						new ItemStack(pageOut),//Output
						NonNullList.from(null,
							Ingredient.fromStacks(new ItemStack(potionIn)),
							Ingredient.fromStacks(new ItemStack(paper))
						)
					).setRegistryName(Properties.modid, recipeName)
				);
			}
		}
	}
	
	private void copyPageToUnboundPage(IForgeRegistry<IRecipe> registry, WarpPageItem page) {
		
		if (page.isWarpCloneable(new ItemStack(page))) {
			String recipeName = upperFirst(page.getRegistryName().getResourcePath()) + "FromCopy";
		
			registry.register(
				new WarpPageShapeless(
					new ItemStack(page, 2),//Output
						NonNullList.from(null,
							Ingredient.fromStacks(new ItemStack(page, 1)),
							Ingredient.fromStacks(new ItemStack(WarpBookMod.items.unboundWarpPageItem, 1)
						)
					)
				).setRegistryName(Properties.modid, recipeName)
			);
		}

	}
	
	private String upperFirst(String stringIn) {
		return stringIn.substring(0,1).toUpperCase() + stringIn.substring(1);
	}
}
