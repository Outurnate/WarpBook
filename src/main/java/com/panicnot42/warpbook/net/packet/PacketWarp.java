package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.container.WarpBookWaypointContainer;
import com.panicnot42.warpbook.util.net.AbstractPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
  public void encode(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    buffer.writeInt(pageSlot);
  }

  @Override
  public void decode(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    pageSlot = buffer.readInt();
  }

  @Override
  public void handleClient(EntityPlayer player)
  {
  }

  @Override
  public void handleServer(EntityPlayer player)
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
