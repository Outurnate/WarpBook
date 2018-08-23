package com.panicnot42.warpbook.core;

import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDeclareWarp {
  /** Used by the warpbook for generating it's listing.  Must be used for warp book pages */
  String getName(World world, ItemStack stack);
  
  /** Gets the waypoint for this object */
  Waypoint getWaypoint(EntityPlayer player, ItemStack stack);
  
  /** Does this stack have valid waypoint data? */
  boolean hasValidData(ItemStack stack);
  
}
