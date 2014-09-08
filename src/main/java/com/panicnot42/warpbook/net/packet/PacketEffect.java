package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.util.net.NetUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PacketEffect implements IMessage, IMessageHandler<PacketEffect, IMessage>
{
  boolean enter;
  int x, y, z;

  public PacketEffect()
  {
  }

  public PacketEffect(boolean enter, int x, int y, int z)
  {
    this.enter = enter;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public IMessage onMessage(PacketEffect message, MessageContext ctx)
  {
    EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
    int particles = (2 - Minecraft.getMinecraft().gameSettings.particleSetting) * 50;
    if (message.enter)
      for (int i = 0; i < (5 * particles); ++i)
        player.worldObj.spawnParticle("largesmoke", message.x, message.y + (player.worldObj.rand.nextDouble() * 2), message.z, (player.worldObj.rand.nextDouble() / 10) - 0.05D, 0D, (player.worldObj.rand.nextDouble() / 10) - 0.05D);
    else
      for (int i = 0; i < particles; ++i)
        player.worldObj.spawnParticle("portal", message.x - 0.5D, message.y + (player.worldObj.rand.nextDouble() * 2), message.z - 0.5D, player.worldObj.rand.nextDouble() - 0.5D, player.worldObj.rand.nextDouble() - 0.5D, player.worldObj.rand.nextDouble() - 0.5D);
    
    return null;
  }

  @Override
  public void fromBytes(ByteBuf buffer)
  {
    enter = buffer.readBoolean();
    x = buffer.readInt();
    y = buffer.readInt();
    z = buffer.readInt();
  }

  @Override
  public void toBytes(ByteBuf buffer)
  {
    buffer.writeBoolean(enter);
    buffer.writeInt(x);
    buffer.writeInt(y);
    buffer.writeInt(z);
  }
}
