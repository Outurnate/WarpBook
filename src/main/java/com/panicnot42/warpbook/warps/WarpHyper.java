package com.panicnot42.warpbook.warps;

import java.util.List;

import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.core.WarpColors;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpHyper extends Warp {
	
	@Override
	public String getName(World world, ItemStack stack) {
		if (hasValidData(stack)) {
			String name = stack.getTagCompound().getString("hypername");
			if(name != null) {
				return name;
			}
		}
		return unbound;
	}
	
	@Override
	public Waypoint getWaypoint(EntityPlayer player, ItemStack stack) {
		if(hasValidData(stack)) {
			WarpWorldStorage storage = WarpWorldStorage.get(player.getEntityWorld());
			return storage.getWaypoint(stack.getTagCompound().getString("hypername"));
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(ttprefix + getName(world, stack));
	}
	
	@Override
	public boolean hasValidData(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("hypername");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public WarpColors getColor() {
		return WarpColors.HYPER;
	}
	
}
