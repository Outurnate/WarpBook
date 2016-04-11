package com.panicnot42.warpbook.core;

import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IDeclareWarp
{
  String GetName(World world, ItemStack stack);
  Waypoint GetWaypoint(EntityPlayer player, ItemStack stack);
  Boolean ValidData(ItemStack stack);
}
