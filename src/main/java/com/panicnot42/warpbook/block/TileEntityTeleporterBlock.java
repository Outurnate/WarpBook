package com.panicnot42.warpbook.block;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTeleporterBlock extends BlockContainer
{
  public TileEntityTeleporterBlock()
  {
    super(Material.iron);
    setUnlocalizedName("teleporter");
    setCreativeTab(WarpBookMod.tabBook);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityTeleporter();
  }
  
  @Override
  public int getRenderType()
  {
    return 3;
  }
}
