package com.panicnot42.warpbook.item;

import java.util.List;

import javax.annotation.Nullable;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotatoWarpPageItem extends Item implements IDeclareWarp
{
  public PotatoWarpPageItem(String name)
  {
    setMaxStackSize(16);
    setCreativeTab(WarpBookMod.tabBook);
    setUnlocalizedName(name);
    setRegistryName(name);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
  {
    ItemStack item = player.getHeldItem(handIn);
    item = new ItemStack(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("minecraft", "poisonous_potato")), 1);
    WarpBookMod.warpDrive.goFullPotato(player, item);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
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
  public void addInformation(ItemStack item, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
  {
    try
    {
      tooltip.add(I18n.format("help.potato.flavour1"));
      tooltip.add(I18n.format("help.potato.flavour2"));
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
