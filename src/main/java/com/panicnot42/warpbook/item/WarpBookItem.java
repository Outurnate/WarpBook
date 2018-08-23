package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.core.WarpColors;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpBookItem extends Item implements IColorable {
	
	public WarpBookItem(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setMaxStackSize(1);
		setCreativeTab(WarpBookMod.tabBook);
		setMaxDamage(0);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 1;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		
		WarpBookMod.lastHeldBooks.put(player, itemStack);
		if (player.isSneaking()) {
			player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookInventoryGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		else {
			player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWarpGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		try {
			tooltip.add(I18n.format("warpbook.booktooltip", stack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId()).tagCount()));
		}
		catch (Exception e) {
			// no pages
		}
	}
	
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return false;
	}
	
	public static int getRespawnsLeft(ItemStack item) {
		if (item.getTagCompound() == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		return item.getTagCompound().getShort("deathPages");
	}
	
	public static void setRespawnsLeft(ItemStack item, int deaths) {
		if (item.getTagCompound() == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		item.getTagCompound().setShort("deathPages", (short)deaths);
	}
	
	public static void decrRespawnsLeft(ItemStack item) {
		setRespawnsLeft(item, getRespawnsLeft(item) - 1);
	}
	
	public static int getCopyCost(ItemStack itemStack) {
		
		if(itemStack.getTagCompound() == null) {
			itemStack.setTagCompound(new NBTTagCompound()); 
		}
		
		NBTTagList items = itemStack.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
		int count = 0;
		for (int i = 0; i < items.tagCount(); ++i) {
			ItemStack item = new ItemStack(items.getCompoundTagAt(i));
			if (((WarpItem)item.getItem()).isWarpCloneable(item)) {
				count += item.getCount();
			}
		}
		return count;
	}
	
	public static ItemStack copyBook(World world, ItemStack book) {
		
		ItemStack stack = new ItemStack(WarpBookMod.items.warpBookItem, 1);
		NBTTagList pages = book.getTagCompound().getTagList("WarpPages", Constants.NBT.TAG_COMPOUND);
		NBTTagList destPages = new NBTTagList();
		for (int i = 0; i < pages.tagCount(); ++i) {
			NBTTagCompound page = pages.getCompoundTagAt(i);
			int slot = page.getInteger("Slot");
			ItemStack item = new ItemStack(page);
			if (item.getItem() instanceof IDeclareWarp && ((WarpItem)item.getItem()).isWarpCloneable(item)) {
				NBTTagCompound tag = new NBTTagCompound();
				item.writeToNBT(tag);
				tag.setInteger("Slot", slot);
				destPages.appendTag(tag);
			}
		}
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("WarpPages", destPages);
		
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int tintIndex) {
		
		switch(tintIndex) {
			case 0: {
				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("color")) {
					return stack.getTagCompound().getInteger("color");
				}
				return WarpColors.LEATHER.getColor();//Leather
			}
			case 1: return WarpColors.UNBOUND.getColor();//The pages
			default: return 0xFFFFFFFF;//The ender pearl
		}
	}
	
}
