package com.panicnot42.warpbook;

import com.panicnot42.warpbook.item.BoundWarpPageItem;
import com.panicnot42.warpbook.item.DeathlyWarpPageItem;
import com.panicnot42.warpbook.item.HyperBoundWarpPageItem;
import com.panicnot42.warpbook.item.PlayerWarpPageItem;
import com.panicnot42.warpbook.item.PotatoWarpPageItem;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpFuelItem;
import com.panicnot42.warpbook.item.WarpPrintingPlateItem;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("warpbook")
@Mod.EventBusSubscriber(modid = "warpbook")
public class WarpItems
{
  @ObjectHolder("warpbook")
  public static final WarpBookItem warpBookItem = null;
  
  @ObjectHolder("playerwarppage")
  public static final PlayerWarpPageItem playerWarpPageItem = null;
  
  @ObjectHolder("hyperwarppage")
  public static final HyperBoundWarpPageItem hyperWarpPageItem = null;
  
  @ObjectHolder("boundwarppage")
  public static final BoundWarpPageItem boundWarpPageItem = null;
  
  @ObjectHolder("unboundwarppage")
  public static final UnboundWarpPageItem unboundWarpPageItem = null;
  
  @ObjectHolder("potatowarppage")
  public static final PotatoWarpPageItem potatoWarpPageItem = null;
  
  @ObjectHolder("deathlywarppage")
  public static final DeathlyWarpPageItem deathlyWarpPageItem = null;
  
  @ObjectHolder("warpfuel")
  public static final WarpFuelItem warpFuelItem = null;
  
  @ObjectHolder("warpplate")
  public static final WarpPrintingPlateItem warpPrintingPlateItem = null;
  
  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event)
  {
    event.getRegistry().registerAll(
        new WarpBookItem("warpbook"),
        new PlayerWarpPageItem("playerwarppage"),
        new HyperBoundWarpPageItem("hyperwarppage"),
        new BoundWarpPageItem("boundwarppage"),
        new UnboundWarpPageItem("unboundwarppage"),
        new PotatoWarpPageItem("potatowarppage"),
        new DeathlyWarpPageItem("deathlywarppage"),
        new WarpPrintingPlateItem("warpplate")
        );
    if (WarpBookMod.fuelEnabled)
      event.getRegistry().registerAll(new WarpFuelItem("warpfuel"));
  }
}
