package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.util.WarpUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UnboundWarpPotionItem extends WarpPotionItem {
	
	public UnboundWarpPotionItem(String name) {
		super(name);
		setCreativeTab(WarpBookMod.tabBook);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking()) {
			stack = WarpUtils.bindItemStackToPlayer(new ItemStack(WarpBookMod.items.playerWarpPotionItem, stack.getCount()), player);
		}
		else {
			WarpUtils.bindItemStackToLocation(new ItemStack(WarpBookMod.items.locusWarpPotionItem), world, player);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
	}
	
}
