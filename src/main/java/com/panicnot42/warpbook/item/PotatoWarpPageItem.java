package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotatoWarpPageItem extends Item implements IDeclareWarp
{
  public PotatoWarpPageItem(String name)
  {
    super.setMaxStackSize(16).setUnlocalizedName(name);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    itemStack = new ItemStack(GameRegistry.findItem("minecraft", "poisonous_potato"), 1);
    WarpBookMod.warpDrive.goFullPotato(player, itemStack);
    return itemStack;
  }

  @Override
  public String GetName(World world, ItemStack stack)
  {
    return null;
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    try
    {
      list.add(I18n.format("help.potato.flavour1"));
      list.add(I18n.format("help.potato.flavour2"));
    }
    catch (Exception e)
    {
    }
  }
  
  @Override
  public Boolean ValidData(ItemStack stack)
  {
    return false;
  }
  
  @Override
  public Boolean WarpCloneable()
  {
    return false;
  }
}
