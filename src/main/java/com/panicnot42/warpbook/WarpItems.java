package com.panicnot42.warpbook;

import com.panicnot42.warpbook.item.DeathlyWarpPageItem;
import com.panicnot42.warpbook.item.LegacyWarpPageItem;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.UnboundWarpPotionItem;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.item.WarpPotionItem;
import com.panicnot42.warpbook.warps.WarpHyper;
import com.panicnot42.warpbook.warps.WarpLocus;
import com.panicnot42.warpbook.warps.WarpPlayer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class WarpItems {
	
	public WarpBookItem warpBookItem;
	public Item warpClusterItem;
	
	public WarpItem playerWarpPageItem;
	public WarpItem hyperWarpPageItem;
	public WarpItem locusWarpPageItem;
	public WarpItem unboundWarpPageItem;
	public WarpItem legacyPageItem;
	public DeathlyWarpPageItem deathlyWarpPageItem;
	
	public WarpItem unboundWarpPotionItem;
	public WarpItem locusWarpPotionItem;
	public WarpItem playerWarpPotionItem;
	public WarpItem hyperWarpPotionItem;
	
	public WarpItems() {
		
		//Misc
		warpBookItem = new WarpBookItem("warpbook");
		warpClusterItem = new WarpItem("warpcluster");
		
		//Pages
		unboundWarpPageItem = new UnboundWarpPageItem("unboundwarppage");
		locusWarpPageItem = new WarpPageItem("boundwarppage").setWarp(new WarpLocus()).setCloneable(true);
		playerWarpPageItem = new WarpPageItem("playerwarppage").setWarp(new WarpPlayer()).setCloneable(false);
		hyperWarpPageItem = new WarpPageItem("hyperwarppage").setWarp(new WarpHyper()).setCloneable(true);
		deathlyWarpPageItem = new DeathlyWarpPageItem("deathlywarppage");
		legacyPageItem = new LegacyWarpPageItem("warppage");
		
		//Potions
		unboundWarpPotionItem = new UnboundWarpPotionItem("unboundwarppotion");
		locusWarpPotionItem = new WarpPotionItem("boundwarppotion").setWarp(new WarpLocus()).setCloneable(true);
		playerWarpPotionItem = new WarpPotionItem("playerwarppotion").setWarp(new WarpPlayer()).setCloneable(true);
		hyperWarpPotionItem = new WarpPotionItem("hyperwarppotion").setWarp(new WarpHyper()).setCloneable(true);
	}
	
	public void register(IForgeRegistry<Item> registry) {
		
		//Misc
		registry.register(warpBookItem);
		registry.register(warpClusterItem);
		
		//Pages
		registry.register(unboundWarpPageItem);
		registry.register(locusWarpPageItem);
		registry.register(playerWarpPageItem);
		registry.register(hyperWarpPageItem);
		registry.register(deathlyWarpPageItem);
		registry.register(legacyPageItem);
		
		//Potions
		registry.register(unboundWarpPotionItem);
		registry.register(locusWarpPotionItem);
		registry.register(playerWarpPotionItem);
		registry.register(hyperWarpPotionItem);
		
		ItemBlock itemBlock = new ItemBlock(WarpBookMod.blocks.bookCloner);
		itemBlock.setRegistryName(WarpBookMod.blocks.bookCloner.getRegistryName());
		registry.register(itemBlock);
		
		itemBlock = new ItemBlock(WarpBookMod.blocks.teleporter);
		itemBlock.setRegistryName(WarpBookMod.blocks.teleporter.getRegistryName());
		registry.register(itemBlock);
	}
	
}
