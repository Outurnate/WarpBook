package com.panicnot42.warpbook;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("warpbook")
@Mod.EventBusSubscriber(modid = "warpbook")
public class WarpSounds
{
  @ObjectHolder("depart")
  public static final SoundEvent departSound = null;
  
  @ObjectHolder("arrive")
  public static final SoundEvent arriveSound = null;

  @SubscribeEvent
  public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
  {
    ResourceLocation departLocation = new ResourceLocation("warpbook", "depart");
    ResourceLocation arriveLocation = new ResourceLocation("warpbook", "arrive");
    
    event.getRegistry().registerAll(
        new SoundEvent(departLocation).setRegistryName(departLocation),
        new SoundEvent(arriveLocation).setRegistryName(arriveLocation)
        );
  }
}
