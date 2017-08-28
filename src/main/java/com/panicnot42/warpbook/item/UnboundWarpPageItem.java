package com.panicnot42.warpbook.item;

import java.math.RoundingMode;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class UnboundWarpPageItem extends Item implements IDeclareWarp
{
  public UnboundWarpPageItem(String name)
  {
    super.setMaxStackSize(64).setCreativeTab(WarpBookMod.tabBook).setUnlocalizedName(name);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
  {
    if (player.isSneaking())
    {
      itemStack.setItem(WarpBookMod.items.playerWarpPageItem);
      itemStack.setTagCompound(new NBTTagCompound());
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
      {
        itemStack.getTagCompound().setString("playeruuid", player.getGameProfile().getId().toString());
        itemStack.getTagCompound().setString("player", player.getGameProfile().getName());
      }
    }
    else
    {
      ItemStack newStack = new ItemStack(WarpBookMod.items.boundWarpPageItem);
      BoundWarpPageItem.Bind(newStack,
                             MathUtils.round(player.posX, RoundingMode.HALF_DOWN),
                             MathUtils.round(player.posY, RoundingMode.HALF_DOWN),
                             MathUtils.round(player.posZ, RoundingMode.HALF_DOWN),
                             player.dimension);
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWaypointGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
      WarpBookMod.formingPages.put(player, newStack);
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
  }

  public String GetName(World world, ItemStack stack)
  {
    return null;
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    return null;
  }
  
  @Override
  public Boolean ValidData(ItemStack stack)
  {
    return false;
  }
  
  @Override
  public Boolean WarpCloneable()
  {
    return true;
  }
}
