package com.panicnot42.warpbook.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.util.net.NetUtils;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
    EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
    --player.getHeldItem().stackSize;
    if (player.getHeldItem().stackSize == 0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
    ItemStack newPage = WarpBookMod.formingPages.get(player);
    newPage.getTagCompound().setString("name", message.name);
    EntityItem item = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, newPage);
    player.worldObj.spawnEntityInWorld(item);
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
