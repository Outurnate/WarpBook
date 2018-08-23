package com.panicnot42.warpbook.warps;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import com.panicnot42.warpbook.core.WarpColors;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpPlayer extends Warp {

	@Override
	public String getName(World world, ItemStack stack) {
		if (hasValidData(stack)) {
			String name = stack.getTagCompound().getString("player");
			if(name != null) {
				return name;
			}
		}
		return unbound;
	}
	
	@Override
	public Waypoint getWaypoint(EntityPlayer player, ItemStack stack) {

		if (!player.world.isRemote) {
			if(hasValidData(stack)) {
				UUID playerID = UUID.fromString(stack.getTagCompound().getString("playeruuid"));
				EntityPlayerMP playerTo = null;
				List<EntityPlayerMP> allPlayers = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
				for (EntityPlayerMP playerS : allPlayers) {
					if (playerS.getUniqueID().equals(playerID)) {
						playerTo = playerS;
					}
				}
				if (player != playerTo && playerTo != null) {
					return new Waypoint("", "",
							MathUtils.round(playerTo.posX, RoundingMode.DOWN),
							MathUtils.round(playerTo.posY, RoundingMode.DOWN),
							MathUtils.round(playerTo.posZ, RoundingMode.DOWN),
							playerTo.dimension);
				}
			}
		}

		return null;
	}
	
	@Override
	public boolean hasValidData(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("playeruuid");

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		tooltip.add(ttprefix + getName(world, stack));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public WarpColors getColor() {
		return WarpColors.PLAYER;
	}
	
}
