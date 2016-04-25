package com.panicnot42.warpbook;

import com.panicnot42.warpbook.block.TileEntityBookClonerBlock;
import com.panicnot42.warpbook.block.TileEntityTeleporterBlock;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class WarpBlocks
{
  public static TileEntityBookClonerBlock bookCloner;
  public static TileEntityTeleporterBlock teleporter;

  public WarpBlocks()
  {
    bookCloner = new TileEntityBookClonerBlock();
    teleporter = new TileEntityTeleporterBlock();
  }

  public void Register()
  {
    GameRegistry.registerTileEntity(TileEntityBookCloner.class, "tileEntityBookCloner");
    
    GameRegistry.registerBlock(bookCloner, "bookcloner");
    GameRegistry.registerBlock(teleporter, "teleporter");
  }
}
