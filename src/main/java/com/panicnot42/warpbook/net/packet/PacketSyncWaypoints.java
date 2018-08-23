package com.panicnot42.warpbook.net.packet;

import java.util.HashMap;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.util.nbt.NBTUtils;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncWaypoints implements IMessage, IMessageHandler<PacketSyncWaypoints, IMessage> {
	public HashMap<String, Waypoint> table;
	
	public PacketSyncWaypoints() {
	}
	
	public PacketSyncWaypoints(HashMap<String, Waypoint> table) {
		this.table = table;
	}
	
	@Override
	public void fromBytes(ByteBuf buffer) {
		NBTTagCompound tag = ByteBufUtils.readTag(buffer);
		table = NBTUtils.readHashMapFromNBT(tag.getTagList("data", Constants.NBT.TAG_COMPOUND), Waypoint.class);
	}
	
	@Override
	public void toBytes(ByteBuf buffer) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTUtils.writeHashMapToNBT(tag.getTagList("data", Constants.NBT.TAG_COMPOUND), table);
		ByteBufUtils.writeTag(buffer, tag);
	}
	
	@Override
	public IMessage onMessage(PacketSyncWaypoints message, MessageContext ctx) {
		World world = Minecraft.getMinecraft().player.world;
		WarpWorldStorage s = WarpWorldStorage.get(world);
		s.table = message.table;
		s.save(world);
		return null;
	}
	
}
