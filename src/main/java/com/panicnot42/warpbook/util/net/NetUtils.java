package com.panicnot42.warpbook.util.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetUtils {
	public static EntityPlayer getPlayerFromContext(MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			return ctx.getServerHandler().player;
		}
		else {
			return getClientPlayer();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
