package com.panicnot42.warpbook;

import com.panicnot42.warpbook.item.BoundWarpPageItem;
import com.panicnot42.warpbook.item.DeathlyWarpPageItem;
import com.panicnot42.warpbook.item.HyperBoundWarpPageItem;
import com.panicnot42.warpbook.item.PlayerWarpPageItem;
import com.panicnot42.warpbook.item.PotatoWarpPageItem;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpFuelItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.item.WarpPrintingPlateItem;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WarpItems
{
  public WarpBookItem warpBookItem;
  public PlayerWarpPageItem playerWarpPageItem;
  public HyperBoundWarpPageItem hyperWarpPageItem;
  public BoundWarpPageItem boundWarpPageItem;
  public UnboundWarpPageItem unboundWarpPageItem;
  public PotatoWarpPageItem potatoWarpPageItem;
  public DeathlyWarpPageItem deathlyWarpPageItem;
  public WarpFuelItem warpFuelItem;
  public WarpPrintingPlateItem warpPrintingPlateItem;
  public WarpPageItem legacyPageItem;

  public WarpItems()
  {
    warpBookItem = new WarpBookItem("warpbook");
    playerWarpPageItem = new PlayerWarpPageItem("playerwarppage");
    hyperWarpPageItem = new HyperBoundWarpPageItem("hyperwarppage");
    boundWarpPageItem = new BoundWarpPageItem("boundwarppage");
    unboundWarpPageItem = new UnboundWarpPageItem("unboundwarppage");
    potatoWarpPageItem = new PotatoWarpPageItem("potatowarppage");
    deathlyWarpPageItem = new DeathlyWarpPageItem("deathlywarppage");
    warpFuelItem = new WarpFuelItem("warpfuel");
    warpPrintingPlateItem = new WarpPrintingPlateItem("warpplate");
    legacyPageItem = new WarpPageItem("warppage");
  }

  @SubscribeEvent
  public void Register()
  {
    GameRegistry.registerItem(warpBookItem, "warpbook");
    GameRegistry.registerItem(playerWarpPageItem, "playerwarppage");
    GameRegistry.registerItem(hyperWarpPageItem, "hyperwarppage");
    GameRegistry.registerItem(boundWarpPageItem, "boundwarppage");
    GameRegistry.registerItem(unboundWarpPageItem, "unboundwarppage");
    GameRegistry.registerItem(potatoWarpPageItem, "potatowarppage");
    GameRegistry.registerItem(deathlyWarpPageItem, "deathlywarppage");
    GameRegistry.registerItem(warpPrintingPlateItem, "warpplate");
    GameRegistry.registerItem(legacyPageItem, "warppage");
    if (WarpBookMod.fuelEnabled)
      GameRegistry.registerItem(warpFuelItem, "warpfuel");
  }
}
