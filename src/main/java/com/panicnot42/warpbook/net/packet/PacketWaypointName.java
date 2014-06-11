package com.panicnot42.warpbook.net.packet;

import com.panicnot42.util.AbstractPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketWaypointName extends AbstractPacket
{
  String name;

  public PacketWaypointName()
  {
  }

  public PacketWaypointName(String name)
  {
    this.name = name;
  }

  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    buffer.writeBytes(name.getBytes());
  }

  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    byte[] data = new byte[buffer.readableBytes()];
    buffer.readBytes(data);
    name = new String(data);
  }

  @Override
  public void handleClientSide(EntityPlayer player)
  {
  }

  @Override
  public void handleServerSide(EntityPlayer player)
  {
    player.getHeldItem().getTagCompound().setString("name", name);
  }
}
