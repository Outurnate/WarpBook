package com.panicnot42.warpbook.warps;

import java.util.List;

import com.panicnot42.warpbook.core.WarpColors;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpLocus extends Warp {
	
	@Override
	public String getName(World world, ItemStack stack) {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("name")) {
			return stack.getTagCompound().getString("name");
		}
		return unbound;
	}
	
	@Override
	public Waypoint getWaypoint(EntityPlayer player, ItemStack stack) {
		if(hasValidData(stack) ) {
			return new Waypoint("", "",
				stack.getTagCompound().getInteger("posX"),
				stack.getTagCompound().getInteger("posY"),
				stack.getTagCompound().getInteger("posZ"),
				stack.getTagCompound().getInteger("dim"));
		} else {
			return null;
		}
	}
	
	@Override
	public boolean hasValidData(ItemStack stack) {
		return stack.hasTagCompound() &&
				stack.getTagCompound().hasKey("posX") &&
				stack.getTagCompound().hasKey("posY") &&
				stack.getTagCompound().hasKey("posZ") &&
				stack.getTagCompound().hasKey("dim");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(ttprefix + getName(world, stack));
		if(hasValidData(stack)) {
			try {
				tooltip.add(I18n.format("warpbook.bindmsg",
					stack.getTagCompound().getInteger("posX"),
					stack.getTagCompound().getInteger("posY"),
					stack.getTagCompound().getInteger("posZ"),
					stack.getTagCompound().getInteger("dim")));
			}
			catch (Exception e) {}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public WarpColors getColor() {
		return WarpColors.BOUND;
	}

}
