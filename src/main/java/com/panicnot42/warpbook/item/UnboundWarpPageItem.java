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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class UnboundWarpPageItem extends Item implements IDeclareWarp
{
  public UnboundWarpPageItem()
  {
    super.setMaxStackSize(16).setCreativeTab(WarpBookMod.tabBook).setUnlocalizedName("unboundwarppage");
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

    @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    if (player.isSneaking())
    {
      itemStack.setItemDamage(5);
      itemStack.setTagCompound(new NBTTagCompound());
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        itemStack.getTagCompound().setString("playeruuid", player.getGameProfile().getId().toString());
    }
    else
    {
      ItemStack newStack = new ItemStack(WarpBookMod.boundWarpPageItem);
      BoundWarpPageItem.Bind(newStack,
                             MathUtils.round(player.posX, RoundingMode.HALF_DOWN),
                             MathUtils.round(player.posY, RoundingMode.HALF_DOWN),
                             MathUtils.round(player.posZ, RoundingMode.HALF_DOWN),
                             player.dimension);
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWaypointGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
      WarpBookMod.formingPages.put(player, newStack);
    }
    return itemStack;
  }

  public String GetName(World world, ItemStack stack)
  {
    return null;
  }
  
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    return null;
  }
  
  public Boolean ValidData(ItemStack stack)
  {
    return false;
  }
}
