package panicnot42.warpbook.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import panicnot42.util.AbstractPacket;

public class PacketParticle extends AbstractPacket
{
  int posX, posY, posZ, dim, pdim;
  double pX, pY, pZ;

  public PacketParticle()
  {
  }

  public PacketParticle(int posX, int posY, int posZ, int dim, double pX, double pY, double pZ, int pdim)
  {
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    this.dim = dim;
    this.pdim = pdim;
    this.pX = pX;
    this.pY = pY;
    this.pX = pZ;
  }

  @Override
  public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    buffer.writeInt(posX);
    buffer.writeInt(posY);
    buffer.writeInt(posZ);
    buffer.writeInt(dim);
    buffer.writeInt(pdim);
    buffer.writeDouble(pX);
    buffer.writeDouble(pY);
    buffer.writeDouble(pZ);
  }

  @Override
  public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
  {
    posX = buffer.readInt();
    posY = buffer.readInt();
    posZ = buffer.readInt();
    dim = buffer.readInt();
    pdim = buffer.readInt();
    pX = buffer.readDouble();
    pY = buffer.readDouble();
    pZ = buffer.readDouble();
  }

  @Override
  public void handleClientSide(EntityPlayer player)
  {
    // DataInputStream inputStream = new DataInputStream(new
    // ByteArrayInputStream(packet.data));
    //
    // try
    // {
    // pX += 0.5f;
    // y = inputStream.readInt() + 0.5f;
    // z = inputStream.readInt() + 0.5f;
    // dim = inputStream.readInt();
    // px = inputStream.readDouble();
    // py = inputStream.readDouble();
    // pz = inputStream.readDouble();
    // pdim = inputStream.readInt();
    // }
    // catch (IOException e)
    // {
    // e.printStackTrace();
    // return;
    // }
    double dposX = posX + 0.5f, dposY = posY + 0.5f, dposZ = posZ + 0.5f;
    for (int i = 0; i < 100; ++i)
    {
      if (player.dimension != pdim) break;
      World world = player.worldObj;
      world.spawnParticle("largesmoke", pX + world.rand.nextDouble() - 0.5, pY + world.rand.nextDouble() - 0.5, pZ + world.rand.nextDouble() - 0.5, (world.rand.nextDouble() - 0.5) / 4,
          (world.rand.nextDouble() - 0.5) / 4, (world.rand.nextDouble() - 0.5) / 4);
    }
    for (int i = 0; i < 100; ++i)
    {
      if (player.dimension != dim) break;
      World world = player.worldObj;
      world.spawnParticle("portal", dposX + world.rand.nextDouble() - 0.5, dposY + world.rand.nextDouble() - 0.5, dposZ + world.rand.nextDouble() - 0.5, (world.rand.nextDouble() - 0.5) / 4,
          world.rand.nextDouble(), (world.rand.nextDouble() - 0.5) / 4);
    }
  }

  @Override
  public void handleServerSide(EntityPlayer player)
  {
  }
}
