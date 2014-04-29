package panicnot42.warpbook.net.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import panicnot42.util.AbstractPacket;
import panicnot42.warpbook.WarpBookMod;

public class PacketWarp extends AbstractPacket
{
  ItemStack itemStack;
  int pageSlot;
  
  public PacketWarp()
  {
  }

  public PacketWarp(ItemStack itemStack, int pageSlot)
  {
    this.pageSlot = pageSlot;
    this.itemStack = itemStack;
  }

  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    buffer.writeInt(pageSlot);
    ByteBufUtils.writeItemStack(buffer, itemStack);
  }

  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    pageSlot = buffer.readInt();
    itemStack = ByteBufUtils.readItemStack(buffer);
  }

  @Override
  public void handleClientSide(EntityPlayer player)
  {
  }

  @Override
  public void handleServerSide(EntityPlayer player)
  {
    ItemStack page = getPageById(itemStack, this.pageSlot);
    WarpBookMod.proxy.handleWarp(player, page);
  }

  public static ItemStack getPageById(ItemStack itemStack, int pageSlot)
  {
    NBTTagList stack = itemStack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    ItemStack page = ItemStack.loadItemStackFromNBT(stack.getCompoundTagAt(pageSlot));
    return page;
  }
}
