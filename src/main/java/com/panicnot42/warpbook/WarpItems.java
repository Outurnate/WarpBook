package com.panicnot42.warpbook;

import com.panicnot42.warpbook.item.BoundWarpPageItem;
import com.panicnot42.warpbook.item.DeathlyWarpPageItem;
import com.panicnot42.warpbook.item.HyperBoundWarpPageItem;
import com.panicnot42.warpbook.item.PlayerWarpPageItem;
import com.panicnot42.warpbook.item.PotatoWarpPageItem;
import com.panicnot42.warpbook.item.UnboundWarpPageItem;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpFuelItem;

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

  public WarpItems()
  {
    warpBookItem = new WarpBookItem();
    playerWarpPageItem = new PlayerWarpPageItem();
    hyperWarpPageItem = new HyperBoundWarpPageItem();
    boundWarpPageItem = new BoundWarpPageItem();
    unboundWarpPageItem = new UnboundWarpPageItem();
    potatoWarpPageItem = new PotatoWarpPageItem();
    deathlyWarpPageItem = new DeathlyWarpPageItem();
    warpFuelItem = new WarpFuelItem();
  }

  public void Register()
  {
    GameRegistry.registerItem(warpBookItem, "warpbook");
    GameRegistry.registerItem(playerWarpPageItem, "playerwarppage");
    GameRegistry.registerItem(hyperWarpPageItem, "hyperwarppage");
    GameRegistry.registerItem(boundWarpPageItem, "boundwarppage");
    GameRegistry.registerItem(unboundWarpPageItem, "unboundwarppage");
    GameRegistry.registerItem(potatoWarpPageItem, "potatowarppage");
    GameRegistry.registerItem(deathlyWarpPageItem, "deathlywarppage");
//    GameRegistry.registerItem(warpFuelItem, "warpfuelitem");
  }
}
