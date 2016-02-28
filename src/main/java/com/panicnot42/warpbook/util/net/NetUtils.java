package com.panicnot42.warpbook.util.net;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetUtils
{
  public static EntityPlayer getPlayerFromContext(MessageContext ctx)
  {
    EntityPlayer player = null;
    switch (FMLCommonHandler.instance().getEffectiveSide())
    {
      case CLIENT:
        player = getClientPlayer();
        break;

      case SERVER:
        INetHandler netHandler = ctx.netHandler; // holy derp
        player = ((NetHandlerPlayServer)netHandler).playerEntity;
        break;

      default:
    }
    return player;
  }

  @SideOnly(Side.CLIENT)
  private static EntityPlayer getClientPlayer()
  {
    return Minecraft.getMinecraft().thePlayer;
  }
}
