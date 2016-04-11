package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HyperBoundWarpPageItem extends Item implements IDeclareWarp
{
  public HyperBoundWarpPageItem()
  {
    super.setMaxStackSize(16).setCreativeTab(WarpBookMod.tabBook).setUnlocalizedName("hyperboundwarppage");
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public String GetName(World world, ItemStack stack)
  {
    WarpWorldStorage storage = WarpWorldStorage.instance(world);
    return storage.getWaypoint(stack.getTagCompound().getString("hypername")).name;
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    WarpWorldStorage storage = WarpWorldStorage.instance(player.getEntityWorld());
    return storage.getWaypoint(stack.getTagCompound().getString("hypername"));
  }

  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    if (!player.isSneaking())
    {
      WarpBookMod.warpDrive.handleWarp(player, itemStack);
      if (!player.capabilities.isCreativeMode)
        --itemStack.stackSize;
    }
    return itemStack;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    if (item.hasTagCompound())
    {
      String name = item.getTagCompound().getString("hypername");
      list.add(name);
      list.add(WarpWorldStorage.instance(player.worldObj).getWaypoint(name).friendlyName);
    }
  }
  
  public Boolean ValidData(ItemStack stack)
  {
    return true;
  }
}
