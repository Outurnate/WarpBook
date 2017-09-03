package com.panicnot42.warpbook.item;

import java.util.List;

import javax.annotation.Nullable;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.util.ITooltipFlag;
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
    setMaxStackSize(16);
    setUnlocalizedName(name);
    setRegistryName(name);
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
    return stack.getTagCompound().getString("hypername");
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    WarpWorldStorage storage = WarpWorldStorage.get(player.getEntityWorld());
    return storage.getWaypoint(stack.getTagCompound().getString("hypername"));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
  {
    ItemStack item = player.getHeldItem(handIn);
    if (!player.isSneaking())
    {
      WarpBookMod.warpDrive.handleWarp(player, item);
      if (!player.capabilities.isCreativeMode)
        item.setCount(item.getCount() - 1);
      
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
    }
    
    return new ActionResult<ItemStack>(EnumActionResult.PASS, item);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    if (item.hasTagCompound())
    {
      String name = item.getTagCompound().getString("hypername");
      tooltip.add(name);
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
