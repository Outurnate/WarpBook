package com.panicnot42.warpbook;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import io.netty.channel.ChannelFutureListener;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

public class WarpWorldStorage extends WorldSavedData {
	public HashMap<String, Waypoint> table;
	public HashMap<UUID, Waypoint> deaths;
	
	private final static String IDENTIFIER = "WarpBook";
	
	public WarpWorldStorage(String identifier) {
		super(identifier);
		if (table == null) {
			table = new HashMap<String, Waypoint>();
		}
		if (deaths == null) {
			deaths = new HashMap<UUID, Waypoint>();
		}
	}
	
	public static WarpWorldStorage get(World world) {
		if (world.getMapStorage().getOrLoadData(WarpWorldStorage.class, IDENTIFIER) == null) {
			world.getMapStorage().setData(IDENTIFIER, new WarpWorldStorage(IDENTIFIER));
		}
		WarpWorldStorage storage = (WarpWorldStorage)world.getMapStorage().getOrLoadData(WarpWorldStorage.class, IDENTIFIER);
		return storage;
	}
	
	public void save(World world) {
		this.markDirty();
		world.getMapStorage().setData(IDENTIFIER, this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound var1) {
		//UUID.fromString(death.getKey())
		//Constants.NBT.TAG_COMPOUND
		NBTTagList waypoints = var1.getTagList("waypoints", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < waypoints.tagCount(); ++i) {
			NBTTagCompound waypoint = waypoints.getCompoundTagAt(i);
			table.put(waypoint.getString("name"),
					new Waypoint(waypoint.getCompoundTag("data")));
		}
		
		NBTTagList deathsNBT = var1.getTagList("deaths", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < deathsNBT.tagCount(); ++i) {
			NBTTagCompound death = deathsNBT.getCompoundTagAt(i);
			deaths.put(UUID.fromString(death.getString("uuid")),
					new Waypoint(death.getCompoundTag("data")));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound var1) {
		NBTTagList waypoints = new NBTTagList();
		for (Entry<String, Waypoint> waypointSource : table.entrySet()) {
			NBTTagCompound waypoint = new NBTTagCompound();
			waypoint.setString("name", waypointSource.getKey());
			NBTTagCompound data = new NBTTagCompound();
			waypointSource.getValue().writeToNBT(data);
			waypoint.setTag("data", data);
			
			waypoints.appendTag(waypoint);
		}
		
		NBTTagList deathsNBT = new NBTTagList();
		for (Entry<UUID, Waypoint> deathSource : deaths.entrySet()) {
			NBTTagCompound death = new NBTTagCompound();
			death.setString("uuid", deathSource.getKey().toString());
			NBTTagCompound data = new NBTTagCompound();
			deathSource.getValue().writeToNBT(data);
			death.setTag("data", data);
			
			deathsNBT.appendTag(death);
		}
		
		var1.setTag("waypoints", waypoints);
		var1.setTag("deaths", deathsNBT);
		return var1;
	}
	
	void updateClient(EntityPlayerMP player, ServerConnectionFromClientEvent e) {
		FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel(Properties.modid, Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DISPATCHER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(e.getManager()));
		channel.writeAndFlush(new PacketSyncWaypoints(table)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	public boolean waypointExists(String name) {
		return table.containsKey(name);
	}
	
	public Waypoint getWaypoint(String name) {
		return table.get(name);
	}
	
	public void addWaypoint(Waypoint point) {
		table.put(point.name, point);
		this.markDirty();
	}
	
	public Set<String> listWaypoints() {
		return table.keySet();
	}
	
	public boolean deleteWaypoint(String waypoint) {
		boolean res = table.remove(waypoint) != null;
		this.markDirty();
		return res;
	}
	
	public void setLastDeath(UUID id, double posX, double posY, double posZ, int dim) {
		deaths.put(id, new Waypoint("Death Point", "death", MathUtils.round(posX, RoundingMode.DOWN), MathUtils.round(posY, RoundingMode.DOWN), MathUtils.round(posZ, RoundingMode.DOWN), dim));
		this.markDirty();
	}
	
	public void clearLastDeath(UUID id) {
		deaths.remove(id);
		this.markDirty();
	}
	
	public Waypoint getLastDeath(UUID id) {
		return deaths.get(id);
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
}
