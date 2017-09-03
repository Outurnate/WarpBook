package com.panicnot42.warpbook;

import com.panicnot42.warpbook.block.TileEntityBookClonerBlock;
import com.panicnot42.warpbook.block.TileEntityTeleporterBlock;
import com.panicnot42.warpbook.item.BoundWarpPageItem;
import com.panicnot42.warpbook.item.DeathlyWarpPageItem;
import com.panicnot42.warpbook.item.HyperBoundWarpPageItem;
import com.panicnot42.warpbook.item.PlayerWarpPageItem;
import com.panicnot42.warpbook.item.PotatoWarpPageItem;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpFuelItem;
import com.panicnot42.warpbook.item.WarpPrintingPlateItem;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("warpbook")
@Mod.EventBusSubscriber(modid = "warpbook")
public class WarpBlocks
{
  @ObjectHolder("bookcloner")
  public static final TileEntityBookClonerBlock bookCloner = null;

  @ObjectHolder("teleporter")
  public static final TileEntityTeleporterBlock teleporter = null;
  
  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event)
  {
    event.getRegistry().registerAll(
        new TileEntityBookClonerBlock("bookcloner"),
        new TileEntityTeleporterBlock("teleporter")
        );
  }
  
  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event)
  {
    event.getRegistry().registerAll(
        new ItemBlock(bookCloner).setRegistryName("bookCloner"),
        new ItemBlock(teleporter).setRegistryName("teleporter")
        );
  }
}
