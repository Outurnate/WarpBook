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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BoundWarpPageItem extends Item implements IDeclareWarp
{
  public BoundWarpPageItem(String name)
  {
    super.setMaxStackSize(16).setUnlocalizedName(name);
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

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
  {
	  ItemStack item = player.getHeldItem(handIn);
    if (player.isSneaking())
    {
      item = new ItemStack(WarpBookMod.items.unboundWarpPageItem, item.getCount());
    }
    else
    {
      WarpBookMod.warpDrive.handleWarp(player, item);
      if (!player.capabilities.isCreativeMode)
      {
        item = new ItemStack(item.getItem(), item.getCount() - 1);
        item.setTagCompound(item.getTagCompound());
      }
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
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

  @Override
  public Boolean ValidData(ItemStack item)
  {
    return item.hasTagCompound() &&
      item.getTagCompound().hasKey("posX") &&
      item.getTagCompound().hasKey("posY") &&
      item.getTagCompound().hasKey("posZ") &&
      item.getTagCompound().hasKey("dim");
  }

  @Override
  public Boolean WarpCloneable()
  {
    return true;
  }
}
