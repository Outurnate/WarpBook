package com.panicnot42.warpbook.block;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityTeleporterBlock extends Block implements ITileEntityProvider
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

  //https://github.com/OpenMods/OpenModsLib/blob/master/src/main/java/openmods/block/OpenBlock.java#L216
  @Override
  public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
  {
    return null;
  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
  {
  }

  @Override
  public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
  {
    return world.setBlockToAir(pos);
  }

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
  {
    return null;
  }
  
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  {
  }
}
