package com.panicnot42.warpbook;

import com.panicnot42.warpbook.block.TileEntityBookClonerBlock;
import com.panicnot42.warpbook.block.TileEntityTeleporterBlock;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("warpbook")
public class WarpBlocks
{
  @ObjectHolder("bookcloner")
  public static final TileEntityBookClonerBlock bookCloner = null;

  @ObjectHolder("teleporter")
  public static final TileEntityTeleporterBlock teleporter = null;
}
