package com.panicnot42.warpbook.net.packet;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.panicnot42.warpbook.util.PlayerUtils;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPlayers implements IMessage, IMessageHandler<PacketSyncPlayers, IMessage>
{
  ArrayList<GameProfile> profiles = new ArrayList<GameProfile>();

  public PacketSyncPlayers()
  {
  }

  public PacketSyncPlayers(ArrayList<GameProfile> profiles)
  {
    this.profiles = profiles;
  }

  @Override
  public void fromBytes(ByteBuf buf)
  {
    while (buf.readableBytes() != 0)
    {
      UUID uuid = new UUID(buf.readLong(), buf.readLong());
      byte[] bytes = new byte[buf.readInt()];
      for (int i = 0; i < bytes.length; ++i)
        bytes[i] = buf.readByte();
      profiles.add(new GameProfile(uuid, new String(bytes)));
    }
  }

  @Override
  public void toBytes(ByteBuf buf)
  {
    for (GameProfile profile : profiles)
    {
      buf.writeLong(profile.getId().getMostSignificantBits());
      buf.writeLong(profile.getId().getLeastSignificantBits());
      byte[] bytes = profile.getName().getBytes();
      buf.writeInt(bytes.length);
      buf.writeBytes(bytes);
    }
  }

  @Override
  public IMessage onMessage(PacketSyncPlayers message, MessageContext ctx)
  {
    PlayerUtils.instance().setProfiles(message.profiles);
    return null;
  }
}
