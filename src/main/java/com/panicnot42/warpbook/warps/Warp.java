package com.panicnot42.warpbook.warps;

import java.util.HashMap;
import java.util.List;

import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.core.WarpColors;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Warp implements IDeclareWarp {

	public static final String unbound = "§4§kUnbound";
	public static final String ttprefix = "§a";

	public static HashMap<String, Class<Warp>> warpRegistry = new HashMap<>();
	
	NBTTagCompound tag;
	
	public Warp() {
		tag = new NBTTagCompound();
	}
	
	public void setTag(NBTTagCompound tag) {
		this.tag  = tag;
	}

	public NBTTagCompound getTag() {
		return tag;
	}
	
	@Override
	public String getName(World world, ItemStack stack) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(ttprefix + getName(world, stack));
	}
	
	@Override
	public Waypoint getWaypoint(EntityPlayer player, ItemStack stack) {
		return null;
	}

	@Override
	public boolean hasValidData(ItemStack stack) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public WarpColors getColor() {
		return WarpColors.UNBOUND;
	}
	
}
