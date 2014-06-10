package com.panicnot42.warpbook.net.packet;

import com.panicnot42.util.AbstractPacket;
import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.container.WarpBookWaypointContainer;

public class PacketWarp extends AbstractPacket
{
  int pageSlot;

  public PacketWarp()
  {
  }

  public PacketWarp(int pageSlot)
  {
    this.pageSlot = pageSlot;
  }

  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    buffer.writeInt(pageSlot);
  }

  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    pageSlot = buffer.readInt();
  }

  @Override
  public void handleClientSide(EntityPlayer player)
  {
  }

  @Override
  public void handleServerSide(EntityPlayer player)
  {
    ItemStack page = getPageById(player, this.pageSlot);
    WarpBookMod.proxy.handleWarp(player, page);
  }

  public static ItemStack getPageById(EntityPlayer player, int pageSlot)
  {
    try
    {
      NBTTagList stack = ((WarpBookWaypointContainer)player.openContainer).heldItem.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
      ItemStack page = ItemStack.loadItemStackFromNBT(stack.getCompoundTagAt(pageSlot));
      return page;
    }
    catch (ClassCastException e)
    {
      return null;
    }
  }
}
