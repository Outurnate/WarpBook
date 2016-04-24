package com.panicnot42.warpbook.block;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    player.openGui(WarpBookMod.instance, WarpBookMod.BookClonerInventoryGuiIndex, world, pos.getX(), pos.getY(), pos.getZ());
    return true;
  }

  @Override
  public int getRenderType()
  {
    return 3;
  }
}
