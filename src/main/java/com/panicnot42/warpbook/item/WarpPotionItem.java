package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpPotionItem extends WarpItem {

	public WarpPotionItem(String name) {
		super(name);
		setMaxStackSize(1);
		setContainerItem(Items.GLASS_BOTTLE);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return hasValidData(stack) ? EnumAction.DRINK : EnumAction.NONE;
    }
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase player) {
		
		if(player instanceof EntityPlayer) {
			WarpBookMod.warpDrive.handleWarp((EntityPlayer) player, stack);
	        return new ItemStack(Items.GLASS_BOTTLE);
		}
		
        return ItemStack.EMPTY;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		
		if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)player;
            CriteriaTriggers.CONSUME_ITEM.trigger(entityplayermp, stack);
            entityplayermp.addStat(StatList.getObjectUseStats(this));
		}
		
		if(!hasValidData(stack)) {
			stack = new ItemStack(Items.GLASS_BOTTLE, stack.getCount());
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
    /**
     * How long it takes to use or consume an item
     */
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }
	
	@Override
	public boolean canGoInBook() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int tintIndex) {
		return tintIndex == 0 ? getWarpColor().getColor() : 0xFFFFFFFF;
	}
	
}
