package panicnot42.warpbook;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (packet.channel.equals("WarpBookWaypoint"))
			handleWaypoint(packet, player);
		if (packet.channel.equals("WarpBookWarp"))
			handleWarp(packet, player);
		if (packet.channel.equals("WarpBookParticle"))
			handleParticle(packet, player);
	}

	private void handleParticle(Packet250CustomPayload packet, Player playerEntity)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.CLIENT)
		{
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
			int dim, pdim;
			double x, y, z, px, py, pz;
			
			try
			{
				x    = inputStream.readInt() + 0.5f;
				y    = inputStream.readInt() + 0.5f;
				z    = inputStream.readInt() + 0.5f;
				dim  = inputStream.readInt();
				px   = inputStream.readDouble();
				py   = inputStream.readDouble();
				pz   = inputStream.readDouble();
				pdim = inputStream.readInt();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return;
			}
			EntityClientPlayerMP player = (EntityClientPlayerMP)playerEntity;
			for (int i = 0; i < 100; ++i)
			{
				if (player.dimension != pdim)
					break;
				World world = player.worldObj;
				world.spawnParticle("largesmoke",
						px + world.rand.nextDouble() - 0.5,
						py + world.rand.nextDouble() - 0.5,
						pz + world.rand.nextDouble() - 0.5,
						(world.rand.nextDouble() - 0.5) / 4,
						(world.rand.nextDouble() - 0.5) / 4,
						(world.rand.nextDouble() - 0.5) / 4);
			}
			for (int i = 0; i < 100; ++i)
			{
				if (player.dimension != dim)
					break;
				World world = player.worldObj;
				world.spawnParticle("portal",
						x + world.rand.nextDouble() - 0.5,
						y + world.rand.nextDouble() - 0.5,
						z + world.rand.nextDouble() - 0.5,
						(world.rand.nextDouble() - 0.5) / 4,
						 world.rand.nextDouble(),
						(world.rand.nextDouble() - 0.5) / 4);
			}
		}
	}

	private void handleWarp(Packet250CustomPayload packet, Player player)
	{
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int pageSlot;
		ItemStack stacki = new ItemStack(Block.dirt);
		
		try
		{
			pageSlot = inputStream.readInt();
			NBTTagCompound tag = CompressedStreamTools.readCompressed(inputStream);
			stacki.readFromNBT(tag);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		EntityPlayerMP mpp = (EntityPlayerMP)player;
		NBTTagList stack = stacki.getTagCompound().getTagList("WarpPages");
		NBTTagCompound page = ItemStack.loadItemStackFromNBT((NBTTagCompound)stack.tagAt(pageSlot)).getTagCompound();

		Packet250CustomPayload packetRet;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	DataOutputStream outputStream = new DataOutputStream(bos);
    	try
    	{
    		outputStream.writeInt(page.getInteger("posX"));
    		outputStream.writeInt(page.getInteger("posY"));
    		outputStream.writeInt(page.getInteger("posZ"));
    		outputStream.writeInt(page.getInteger("dim"));
    		outputStream.writeDouble(mpp.posX);
    		outputStream.writeDouble(mpp.posY);
    		outputStream.writeDouble(mpp.posZ);
    		outputStream.writeInt(mpp.dimension);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	packet.channel = "WarpBookParticle";
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();
		PacketDispatcher.sendPacketToAllPlayers(packet);
		
		if (mpp.dimension != page.getInteger("dim"))
			mpp.travelToDimension(page.getInteger("dim"));
		mpp.setPositionAndUpdate(page.getInteger("posX") + 0.5f, page.getInteger("posY") + 0.5f, page.getInteger("posZ") + 0.5f);
	}

	private void handleWaypoint(Packet250CustomPayload packet, Player player)
	{
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		String waypointName;
		
		try
		{
			BufferedReader d = new BufferedReader(new InputStreamReader(inputStream));
			waypointName = d.readLine();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		ItemStack stack = ((EntityPlayerMP)player).getHeldItem();
		stack.getTagCompound().setString("name", waypointName);
	}
}
