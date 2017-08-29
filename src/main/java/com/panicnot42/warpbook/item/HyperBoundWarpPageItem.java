package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HyperBoundWarpPageItem extends Item implements IDeclareWarp
{
  public HyperBoundWarpPageItem(String name)
  {
    super.setMaxStackSize(16).setUnlocalizedName(name);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public String GetName(World world, ItemStack stack)
  {
    WarpWorldStorage storage = WarpWorldStorage.get(world);
    return storage.getWaypoint(stack.getTagCompound().getString("hypername")).name;
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    WarpWorldStorage storage = WarpWorldStorage.get(player.getEntityWorld());
    return storage.getWaypoint(stack.getTagCompound().getString("hypername"));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
  {
    if (!player.isSneaking())
    {
      WarpBookMod.warpDrive.handleWarp(player, itemStack);
      if (!player.capabilities.isCreativeMode)
        --itemStack.stackSize;
      
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
    }
    
    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
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
    }
  }
  
  @Override
  public Boolean ValidData(ItemStack stack)
  {
    return stack.getTagCompound().hasKey("hypername");
  }
  
  @Override
  public Boolean WarpCloneable()
  {
    return true;
  }
}
