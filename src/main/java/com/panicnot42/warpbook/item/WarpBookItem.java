package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.container.WarpBookWaypointContainer;
import com.sun.org.apache.xml.internal.security.utils.I18n;

public class WarpBookItem extends Item
{
  public WarpBookItem()
  {
    this.setMaxStackSize(1).setCreativeTab(CreativeTabs.tabTransport).setUnlocalizedName("warpbook").setTextureName("warpbook:warpbook");
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
      player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookInventoryGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
    else
    {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWarpGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
      else
      {
        player.openContainer = new WarpBookWaypointContainer(player.getHeldItem());
        player.openContainer.addCraftingToCrafters((EntityPlayerMP)player);
      }
    }
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
}
