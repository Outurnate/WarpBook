package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.util.net.NetUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketWaypointName implements IMessage, IMessageHandler<PacketWaypointName, IMessage>
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
  public IMessage onMessage(PacketWaypointName message, MessageContext ctx)
  {
    NetUtils.getPlayerFromContext(ctx).getHeldItem().getTagCompound().setString("name", message.name);
    return null;
  }

  @Override
  public void fromBytes(ByteBuf buffer)
  {
    byte[] data = new byte[buffer.readableBytes()];
    buffer.readBytes(data);
    name = new String(data);
  }

  @Override
  public void toBytes(ByteBuf buffer)
  {
    buffer.writeBytes(name.getBytes());
  }
}
