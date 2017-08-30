package com.panicnot42.warpbook.core;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Iterator;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class WarpDrive
{
  public void handleWarp(EntityPlayer player, ItemStack page)
  {
    if (page.getItem() instanceof IDeclareWarp && !player.worldObj.isRemote)
    {
      Waypoint wp = ((IDeclareWarp)page.getItem()).GetWaypoint(player, page);
      if (wp == null) // TODO only for type 5
      {
        if (player.worldObj.isRemote)
          CommandUtils.showError(player, I18n.format("help.waypointnotexist"));
        return;
      }
      boolean crossDim = player.dimension != wp.dim;
      PacketEffect oldDim = new PacketEffect(true, MathUtils.round(player.posX, RoundingMode.DOWN), MathUtils.round(player.posY, RoundingMode.DOWN), MathUtils.round(player.posZ, RoundingMode.DOWN));
      PacketEffect newDim = new PacketEffect(false, wp.x, wp.y, wp.z);
      NetworkRegistry.TargetPoint oldPoint = new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64);
      NetworkRegistry.TargetPoint newPoint = new NetworkRegistry.TargetPoint(wp.dim, wp.x, wp.y, wp.z, 64);
      if (Arrays.asList(WarpBookMod.disabledDestinations).contains(new Integer(wp.dim)))
      {
        CommandUtils.showError(player, I18n.format("help.cantgoto"));
        return;
      }
      if (Arrays.asList(WarpBookMod.disabledLeaving).contains(new Integer(player.dimension)))
      {
        CommandUtils.showError(player, I18n.format("help.cantleave"));
        return;
      }
      
      player.addExhaustion(calculateExhaustion(player.getEntityWorld().getDifficulty(), WarpBookMod.exhaustionCoefficient, crossDim));
      if (!player.worldObj.isRemote)
        teleportPlayer((EntityPlayerMP)player, wp.dim, wp.x - 0.5f, wp.y + 0.5f, wp.z - 0.5f);
      WarpBookMod.network.sendToAllAround(oldDim, oldPoint);
      WarpBookMod.network.sendToAllAround(newDim, newPoint);
    }
  }

  private static float calculateExhaustion(EnumDifficulty difficultySetting, float exhaustionCoefficient, boolean crossDim)
  {
    float scaleFactor = 0.0f;
    switch (difficultySetting)
    {
      case EASY:
        scaleFactor = 1.0f;
        break;
      case NORMAL:
        scaleFactor = 1.5f;
        break;
      case HARD:
        scaleFactor = 2.0f;
        break;
      case PEACEFUL:
        scaleFactor = 0.0f;
        break;
    }
    return exhaustionCoefficient * scaleFactor * (crossDim ? 2.0f : 1.0f);
  }

  public void goFullPotato(EntityPlayer player, ItemStack itemStack)
  {
    DamageSource potato = new DamageSource("potato");
    potato.setDamageAllowedInCreativeMode();
    potato.setDamageBypassesArmor();
    potato.setDamageIsAbsolute();

    player.worldObj.newExplosion(null, player.posX, player.posY, player.posZ, 12, true, true);

    player.attackEntityFrom(potato, player.getMaxHealth());
  }
  
  public static void teleportPlayer(EntityPlayerMP player, int dimension, double x, double y, double z)
  {
    player.fallDistance = 0.0f;
    
    if (dimension == player.dimension)
    {
      player.setPositionAndUpdate(x, y, z);
      return;
    }
    
    int oldDim = player.dimension;
    float rotationYaw = player.rotationYaw;
    float rotationPitch = player.rotationPitch;
    MinecraftServer server = player.worldObj.getMinecraftServer();
    
    WorldServer fromWorld = server.worldServerForDimension(oldDim);
    WorldServer toWorld   = server.worldServerForDimension(dimension);
    fromWorld.getMinecraftServer().getPlayerList().transferPlayerToDimension(player, dimension, new WarpBookTeleporter(toWorld, x, y, z));
    //fromWorld.removeEntity(player);
    player.setPositionAndUpdate(x, y, z);
    if (oldDim == 1) // according to McJty, this is needed for leaving the end
    {
      player.setPositionAndUpdate(x, y, z);
      toWorld.spawnEntityInWorld(player);
      toWorld.updateEntityWithOptionalForce(player, false);
    }
  }
}
