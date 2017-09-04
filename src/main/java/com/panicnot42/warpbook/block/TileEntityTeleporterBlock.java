package com.panicnot42.warpbook.block;

import java.util.List;

import javax.annotation.Nullable;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;
import com.panicnot42.warpbook.util.WorldUtils;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTeleporterBlock extends Block implements ITileEntityProvider
{
  protected static final AxisAlignedBB AABB         = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
  protected static final AxisAlignedBB TRIGGER_AABB = new AxisAlignedBB(0.125D,  0.0D, 0.125D,  0.875D,  0.25D,   0.875D);
  public static final PropertyBool ACTIVE = PropertyBool.create("active");
  
  public TileEntityTeleporterBlock(String name)
  {
    super(Material.IRON);
    setUnlocalizedName(name);
    setRegistryName(name);
    setCreativeTab(WarpBookMod.tabBook);
    setSoundType(SoundType.STONE);
    setHardness(10.0f);
    setResistance(20.0f);
    setHarvestLevel("pickaxe", 2);
  }

  @Override
  public TileEntity createNewTileEntity(World world, int meta)
  {
    return new TileEntityTeleporter();
  }

  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, new IProperty[] { ACTIVE });
  }

  @Override
  public IBlockState getStateFromMeta(int meta)
  {
    return getDefaultState().withProperty(ACTIVE, meta == 1);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return ((Boolean)state.getValue(ACTIVE)) ? 1 : 0;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  {
    if (!player.isSneaking())
    {
      TileEntityTeleporter teleporter = (TileEntityTeleporter)world.getTileEntity(pos);
      if (state.getValue(ACTIVE))
      {
        ItemStack stack = teleporter.GetPage();
        stack.setCount(1);
        if (!world.isRemote)
          WorldUtils.dropItemStackInWorld(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        teleporter.SetPage(null);
        world.setBlockState(pos, state.withProperty(ACTIVE, false));
      }
      else if (player.getHeldItemMainhand() != null &&
               player.getHeldItemMainhand().getItem() instanceof IDeclareWarp &&
               ((IDeclareWarp)player.getHeldItemMainhand().getItem()).WarpCloneable())
      {
        teleporter.SetPage(player.getHeldItemMainhand());
        if (player.getHeldItemMainhand().getCount() < 1)
          player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
        else
          player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        world.setBlockState(pos, state.withProperty(ACTIVE, true));
      }
      teleporter.markDirty();
    }
    return true;
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
  {
    if (!worldIn.<Entity>getEntitiesWithinAABB(EntityPlayer.class, TRIGGER_AABB.offset(pos)).isEmpty())
    {
      ItemStack page = ((TileEntityTeleporter)worldIn.getTileEntity(pos)).GetPage();
      if (page != null)
        WarpBookMod.warpDrive.handleWarp((EntityPlayer)entityIn, page);
    }
  }
  
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    return AABB;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state)
  {
	return false;
  }

  @Override
  public boolean isFullCube(IBlockState state)
  {
    return false;
  }

  @Override
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
  {
    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state)
  {
    if (!world.isRemote)
    {
      ItemStack stack = ((TileEntityTeleporter)world.getTileEntity(pos)).GetPage();
      stack.setCount(1);
      WorldUtils.dropItemStackInWorld(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
  }
}
