package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.core.WarpColors;
import com.panicnot42.warpbook.util.Waypoint;
import com.panicnot42.warpbook.warps.Warp;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpItem extends Item implements IDeclareWarp, IColorable {
	
	public static final String unbound = "§4§kUnbound";
	public static final String ttprefix = "§a";
	
	public Warp warp = new Warp();
	public boolean cloneable = false;;
	
	public WarpItem(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	public WarpItem setWarp(Warp warp) {
		this.warp = warp;
		return this;
	}

	public WarpItem setCloneable(boolean is) {
		this.cloneable = is;
		return this;
	}
	
	@Override
	public String getName(World world, ItemStack stack) {
		return warp.getName(world, stack);
	}
	
	@Override
	public Waypoint getWaypoint(EntityPlayer player, ItemStack stack) {
		return warp.getWaypoint(player, stack);
	}
	
	@Override
	public boolean hasValidData(ItemStack stack) {
		return warp.hasValidData(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		warp.addInformation(stack, world, tooltip, flagIn);
	}
	
	/** Can this be copied to a page? Either in the book cloner or via a copy recipe */
	public boolean isWarpCloneable(ItemStack stack) {
		return cloneable;
	}

	public boolean canGoInBook() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int tintIndex) {
		return 0xFFFFFFFF;
	}
	
	@SideOnly(Side.CLIENT)
	public WarpColors getWarpColor() {
		return warp.getColor();
	}
	
}