package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.BookClonerInventoryItem;
import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.container.BookClonerContainerItem;
import com.panicnot42.warpbook.inventory.container.WarpBookContainerItem;
import com.panicnot42.warpbook.inventory.container.WarpBookSpecialInventory;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler
{
  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    if (ID == WarpBookMod.WarpBookInventoryGuiIndex)
      return new WarpBookContainerItem(player, player.inventory,
                                       new WarpBookInventoryItem(player.getHeldItemMainhand()), new WarpBookSpecialInventory(player.getHeldItemMainhand()));
    if (ID == WarpBookMod.BookClonerInventoryGuiIndex)
      return new BookClonerContainerItem(player, player.inventory,
                                         new BookClonerInventoryItem((TileEntityBookCloner)world.getTileEntity(new BlockPos(x, y, z))));
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
    if (ID == WarpBookMod.WarpBookWarpGuiIndex)
      return new GuiBook(player);
    if (ID == WarpBookMod.WarpBookInventoryGuiIndex)
      return new GuiWarpBookItemInventory(new WarpBookContainerItem(player, player.inventory,
                                                                    new WarpBookInventoryItem(player.getHeldItemMainhand()),
                                                                    new WarpBookSpecialInventory(player.getHeldItemMainhand())));
    if (ID == WarpBookMod.WarpBookWaypointGuiIndex)
      return new GuiWaypointName(player);
    if (ID == WarpBookMod.BookClonerInventoryGuiIndex)
      return new GuiBookClonerItemInventory(new BookClonerContainerItem(player, player.inventory,
                                                                        new BookClonerInventoryItem((TileEntityBookCloner)
                                                                                                    world.getTileEntity(new BlockPos(x, y, z)))));
    return null;
  }
}
