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
    NBTTagList stack = player.getHeldItem().getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    NBTTagCompound page = ItemStack.loadItemStackFromNBT((NBTTagCompound)stack.getCompoundTagAt(pageSlot)).getTagCompound();

    PacketParticle packet = new PacketParticle(page.getInteger("posX"), page.getInteger("posY"), page.getInteger("posZ"), page.getInteger("dim"), player.posX, player.posY, player.posZ,
        player.dimension);

    // Packet250CustomPayload packetRet;
    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // DataOutputStream outputStream = new DataOutputStream(bos);
    // try
    // {
    // outputStream.writeInt(page.getInteger("posX"));
    // outputStream.writeInt(page.getInteger("posY"));
    // outputStream.writeInt(page.getInteger("posZ"));
    // outputStream.writeInt(page.getInteger("dim"));
    // outputStream.writeDouble(mpp.posX);
    // outputStream.writeDouble(mpp.posY);
    // outputStream.writeDouble(mpp.posZ);
    // outputStream.writeInt(mpp.dimension);
    // }
    // catch (Exception e)
    // {
    // e.printStackTrace();
    // }
    // packet.channel = "WarpBookParticle";
    // packet.data = bos.toByteArray();
    // packet.length = bos.size();
    // PacketDispatcher.sendPacketToAllPlayers(packet);

    WarpBookMod.packetPipeline.sendToAll(packet);

    WarpPageItem.doPageWarp(player, page);
  }
}
