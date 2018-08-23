package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.WarpColors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpPageItem extends WarpItem {
	
	public WarpPageItem(String name) {
		super(name);
		setMaxStackSize(64);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		
		WarpBookMod.warpDrive.handleWarp(player, itemStack);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	}
	
	@Override
	public boolean canGoInBook() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int tintIndex) {
		switch(tintIndex) {
			case 0: return pageColor();
			case 1: return symbolColor();
			default: return 0x00FFFFFF;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public int pageColor() {
		return WarpColors.UNBOUND.getColor();
	}
	
	@SideOnly(Side.CLIENT)
	public int symbolColor() {
		return getWarpColor().getColor();
	}

}
