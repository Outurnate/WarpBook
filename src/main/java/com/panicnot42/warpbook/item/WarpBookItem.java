package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpBookItem extends Item
{
  public WarpBookItem(String name)
  {
    this.setMaxStackSize(1).setCreativeTab(WarpBookMod.tabBook).setUnlocalizedName(name).setMaxDamage(WarpBookMod.fuelEnabled ? 16 : 0);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
  {
    ItemStack item = player.getHeldItem(handIn);
    WarpBookMod.lastHeldBooks.put(player, item);
    if (player.isSneaking())
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookInventoryGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
    else
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWarpGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    try
    {
      list.add(I18n.format("warpbook.booktooltip", item.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId()).tagCount()));
    }
    catch (Exception e)
    {
      // no pages
    }
  }

  public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
  {
    return false;
  }

  public static int getRespawnsLeft(ItemStack item)
  {
    if (item.getTagCompound() == null)
      item.setTagCompound(new NBTTagCompound());
    return item.getTagCompound().getShort("deathPages");
  }

  public static int getFuelLeft(ItemStack item)
  {
    return 64 - item.getItemDamage();
  }

  public static void setRespawnsLeft(ItemStack item, int deaths)
  {
    if (item.getTagCompound() == null)
      item.setTagCompound(new NBTTagCompound());
    item.getTagCompound().setShort("deathPages", (short)deaths);
  }

  public static void setFuelLeft(ItemStack item, int fuel)
  {
    item.setItemDamage(64 - fuel);
  }

  public static void decrRespawnsLeft(ItemStack item)
  {
    setRespawnsLeft(item, getRespawnsLeft(item) - 1);
  }

  public static void decrFuelLeft(ItemStack item)
  {
    setFuelLeft(item, getFuelLeft(item) - 1);
  }

  public static int getCopyCost(ItemStack itemStack)
  {
    NBTTagList items = itemStack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    int count = 0;
    for (int i = 0; i < items.tagCount(); ++i)
    {
      ItemStack item = new ItemStack(items.getCompoundTagAt(i));
      if (((IDeclareWarp)item.getItem()).WarpCloneable())
        count += item.getCount();
    }
    return count;
  }
}
