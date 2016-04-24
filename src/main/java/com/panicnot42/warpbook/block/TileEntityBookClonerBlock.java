package com.panicnot42.warpbook.block;

import com.panicnot42.warpbook.Properties;
import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBookClonerBlock extends BlockContainer
{
  public TileEntityBookClonerBlock()
  {
    super(Material.iron);
    setUnlocalizedName("bookcloner");
    setCreativeTab(WarpBookMod.tabBook);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityBookCloner();
  }

  @Override
  public int getRenderType()
  {
    return -1;
  }

  @Override
  public boolean isOpaqueCube()
  {
    return false;
  }

  public boolean renderAsNormalBlock()
  {
    return false;
  }

  /*public void registerIcons(IIconRegister icons)
  {
    this.blockIcon = icons.registerIcon("warpbook:none");
    }*/
}
