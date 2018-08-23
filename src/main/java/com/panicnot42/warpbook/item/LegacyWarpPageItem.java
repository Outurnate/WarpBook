package com.panicnot42.warpbook.item;

import java.util.List;

import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LegacyWarpPageItem extends WarpPageItem {
	//private static final String[] itemMetaNames = new String[] { "unbound", "bound", "hyperbound", "deathly", "potato", "player" };

	public LegacyWarpPageItem(String name) {
		super(name);
		setHasSubtypes(true);
		setCreativeTab(null);
		setMaxDamage(0);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemStack = player.getHeldItem(hand);
		
		ItemStack newStack;
		switch (itemStack.getItemDamage()) {
			default:
			case 0:
				newStack = new ItemStack(WarpBookMod.items.unboundWarpPageItem, itemStack.getCount());
				break;
			case 1:
				newStack = new ItemStack(WarpBookMod.items.locusWarpPageItem, itemStack.getCount());
				break;
			case 2:
				newStack = new ItemStack(WarpBookMod.items.hyperWarpPageItem, itemStack.getCount());
				break;
			case 3:
				newStack = new ItemStack(WarpBookMod.items.deathlyWarpPageItem, itemStack.getCount());
				break;
			case 5:
				newStack = new ItemStack(WarpBookMod.items.playerWarpPageItem, itemStack.getCount());
				break;
		}
		if (itemStack.hasTagCompound()) {
			newStack.setTagCompound(itemStack.getTagCompound());
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, newStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("help.legacy"));
	}
	
	@SideOnly(Side.CLIENT)
	public int pageColor() {
		return 0x00BBBBBB;
	}
	
	@SideOnly(Side.CLIENT)
	public int symbolColor() {
		return 0x00666666;
	}
	
}
