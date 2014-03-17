package panicnot42.warpbook.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import panicnot42.util.AbstractPacket;
import panicnot42.warpbook.WarpBookMod;
import panicnot42.warpbook.item.WarpPageItem;

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
    NBTTagList stack = player.getHeldItem().getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    ItemStack page = ItemStack.loadItemStackFromNBT((NBTTagCompound)stack.getCompoundTagAt(pageSlot));
    return page;
  }
}
