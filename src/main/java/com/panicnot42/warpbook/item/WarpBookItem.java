package com.panicnot42.warpbook.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.panicnot42.warpbook.WarpBookMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WarpBookItem extends Item
{
  public WarpBookItem()
  {
    this.setMaxStackSize(1).setCreativeTab(WarpBookMod.tabBook).setUnlocalizedName("warpbook").setTextureName("warpbook:warpbook").setMaxDamage(WarpBookMod.fuelEnabled ? 16 : 0);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    WarpBookMod.lastHeldBooks.put(player, itemStack);
    if (player.isSneaking())
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookInventoryGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
    else
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWarpGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
    return itemStack;
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
    if (item.getTagCompound() == null) item.setTagCompound(new NBTTagCompound());
    return item.getTagCompound().getShort("deathPages");
  }

  public static int getFuelLeft(ItemStack item)
  {
    return 16 - item.getItemDamage();
  }

  public static void setRespawnsLeft(ItemStack item, int deaths)
  {
    if (item.getTagCompound() == null) item.setTagCompound(new NBTTagCompound());
    item.getTagCompound().setShort("deathPages", (short)deaths);
  }

  public static void setFuelLeft(ItemStack item, int fuel)
  {
    item.setItemDamage(16 - fuel);
  }

  public static void decrRespawnsLeft(ItemStack item)
  {
    setRespawnsLeft(item, getRespawnsLeft(item) - 1);
  }

  public static void decrFuelLeft(ItemStack item)
  {
    setFuelLeft(item, getFuelLeft(item) - 1);
  }
}
