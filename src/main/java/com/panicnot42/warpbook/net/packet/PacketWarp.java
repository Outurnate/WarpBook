package com.panicnot42.warpbook.net.packet;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.util.net.NetUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PacketWarp implements IMessage, IMessageHandler<PacketWarp, IMessage>
{
  int pageSlot;

  public PacketWarp()
  {
  }

  public PacketWarp(int pageSlot)
  {
    this.pageSlot = pageSlot;
  }

  public static ItemStack getPageById(EntityPlayer player, int pageSlot)
  {
    try
    {
      NBTTagList stack = WarpBookMod.lastHeldBooks.get(player).getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
      ItemStack page = ItemStack.loadItemStackFromNBT(stack.getCompoundTagAt(pageSlot));
      return page;
    }
    catch (ClassCastException e)
    {
      return null;
    }
  }

  @Override
  public IMessage onMessage(PacketWarp message, MessageContext ctx)
  {
    EntityPlayer player = NetUtils.getPlayerFromContext(ctx);
    ItemStack page = getPageById(player, this.pageSlot);
    WarpBookMod.proxy.handleWarp(player, page);
    
    return null;
  }

  @Override
  public void fromBytes(ByteBuf buffer)
  {
    pageSlot = buffer.readInt();
  }

  @Override
  public void toBytes(ByteBuf buffer)
  {
    buffer.writeInt(pageSlot);
  }
}
