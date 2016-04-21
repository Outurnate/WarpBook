package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BoundWarpPageItem extends Item implements IDeclareWarp
{
  public BoundWarpPageItem()
  {
    super.setMaxStackSize(16).setUnlocalizedName("boundwarppage");
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  public static void Bind(ItemStack page, int x, int y, int z, int dim)
  {
    if (!page.hasTagCompound())
      page.setTagCompound(new NBTTagCompound());
    page.getTagCompound().setInteger("posX", x);
    page.getTagCompound().setInteger("posY", y);
    page.getTagCompound().setInteger("posZ", z);
    page.getTagCompound().setInteger("dim", dim);
  }

  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    if (player.isSneaking())
    {
      itemStack.setItem(WarpBookMod.unboundWarpPageItem);
    }
    else
    {
      WarpBookMod.warpDrive.handleWarp(player, itemStack);
      if (!player.capabilities.isCreativeMode)
        --itemStack.stackSize;
    }
    return itemStack;
  }

  @Override
  public String GetName(World world, ItemStack stack)
  {
    return stack.getTagCompound().getString("name");
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    return new Waypoint("", "",
                        stack.getTagCompound().getInteger("posX"),
                        stack.getTagCompound().getInteger("posY"),
                        stack.getTagCompound().getInteger("posZ"),
                        stack.getTagCompound().getInteger("dim"));
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    try
    {
      list.add(item.getTagCompound().getString("name"));
      list.add(I18n.format("warpbook.bindmsg",
                           item.getTagCompound().getInteger("posX"),
                           item.getTagCompound().getInteger("posY"),
                           item.getTagCompound().getInteger("posZ"),
                           item.getTagCompound().getInteger("dim")));
    }
    catch (Exception e)
    {
    }
  }
  
  public Boolean ValidData(ItemStack stack)
  {
    return true;
  }
}
