package com.panicnot42.warpbook.block;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTeleporterBlock extends Block implements ITileEntityProvider
{
  public static final PropertyBool ACTIVE = PropertyBool.create("active");
  
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
  protected BlockState createBlockState()
  {
    return new BlockState(this, new IProperty[] { ACTIVE });
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

  public static EntityItem dropItemStackInWorld(World worldObj, double x, double y, double z, ItemStack stack)
  {
    float f = 0.7F;
    float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
    float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
    float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
    EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
    //entityitem.delayBeforeCanPickup = 10;
    if (stack.hasTagCompound())
    {
      entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
    }
    worldObj.spawnEntityInWorld(entityitem);
    return entityitem;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
  {
    if (!player.isSneaking())
    {
      TileEntityTeleporter teleporter = (TileEntityTeleporter)world.getTileEntity(pos);
      if (state.getValue(ACTIVE))
      {
        ItemStack stack = teleporter.GetPage();
        stack.stackSize = 1;
        if (!world.isRemote)
          dropItemStackInWorld(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        teleporter.SetPage(null);
        world.setBlockState(pos, state.withProperty(ACTIVE, false));
      }
      else if (player.getHeldItem() != null &&
               player.getHeldItem().getItem() instanceof IDeclareWarp &&
               ((IDeclareWarp)player.getHeldItem().getItem()).WarpCloneable())
      {
        teleporter.SetPage(player.getHeldItem());
        if (player.getHeldItem().stackSize < 1)
          --player.getHeldItem().stackSize;
        else
          player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        world.setBlockState(pos, state.withProperty(ACTIVE, true));
      }
      world.markBlockForUpdate(pos);
      teleporter.markDirty();
    }
    return true;
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
  {
    if (entityIn instanceof EntityPlayer)
    {
      ItemStack page = ((TileEntityTeleporter)worldIn.getTileEntity(pos)).GetPage();
      if (page != null)
        WarpBookMod.warpDrive.handleWarp((EntityPlayer)entityIn, page);
    }
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
  {
    float f = 0.0625F;
    return new AxisAlignedBB((double)((float)pos.getX() + f),
                             (double)pos.getY(),
                             (double)((float)pos.getZ() + f),
                             (double)((float)(pos.getX() + 1) - f),
                             (double)((float)(pos.getY() + 1) - f),
                             (double)((float)(pos.getZ() + 1) - f));
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
  {
    float f = 0.0625F;
    return new AxisAlignedBB((double)((float)pos.getX() + f), (double)pos.getY(), (double)((float)pos.getZ() + f), (double)((float)(pos.getX() + 1) - f), (double)(pos.getY() + 1), (double)((float)(pos.getZ() + 1) - f));
  }
  
  @Override
  public boolean isFullCube()
  {
    return false;
  }

  @Override
  public boolean isOpaqueCube()
  {
    return false;
  }
}
