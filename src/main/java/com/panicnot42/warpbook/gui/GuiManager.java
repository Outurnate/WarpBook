package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.container.WarpBookContainerItem;
import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler
{
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    if (ID == WarpBookMod.WarpBookInventoryGuiIndex) return new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(player.getHeldItem()), new WarpBookSpecialInventory(player.getHeldItem()));
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    if (ID == WarpBookMod.WarpBookWarpGuiIndex) return new GuiBook(player);
    if (ID == WarpBookMod.WarpBookInventoryGuiIndex) return new GuiWarpBookItemInventory(new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(player.getHeldItem()), new WarpBookSpecialInventory(player.getHeldItem())));
    if (ID == WarpBookMod.WarpBookWaypointGuiIndex) return new GuiWaypointName(player);
    return null;
  }
}
