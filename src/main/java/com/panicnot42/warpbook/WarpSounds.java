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

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WarpSounds
{
  public SoundEvent departSound;
  public SoundEvent arriveSound;

  public WarpSounds()
  {
    ResourceLocation departLocation = new ResourceLocation("warpbook", "depart");
    ResourceLocation arriveLocation = new ResourceLocation("warpbook", "arrive");
    
    departSound = new SoundEvent(departLocation);
    arriveSound = new SoundEvent(arriveLocation);
    
    departSound.setRegistryName(departLocation);
    arriveSound.setRegistryName(arriveLocation);
  }

  @SubscribeEvent
  public void Register()
  {
    GameRegistry.register(departSound);
    GameRegistry.register(arriveSound);
  }
}
