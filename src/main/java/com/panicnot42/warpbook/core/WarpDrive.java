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
import net.minecraft.util.SoundCategory;
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
    if (page.getItem() instanceof IDeclareWarp && !player.world.isRemote)
    {
      Waypoint wp = ((IDeclareWarp)page.getItem()).GetWaypoint(player, page);
      if (wp == null) // TODO only for type 5
      {
        if (player.world.isRemote)
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
      {
        if (crossDim)
          transferPlayerToDimension((EntityPlayerMP)player, wp.x - 0.5f, wp.y + 0.5f, wp.z - 0.5f, wp.dim, player.getServer().getPlayerList());
        else
          player.setPositionAndUpdate(wp.x - 0.5f, wp.y + 0.5f, wp.z - 0.5f);
      }
      player.addExhaustion(calculateExhaustion(player.getEntityWorld().getDifficulty(), WarpBookMod.exhaustionCoefficient, crossDim));
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

    player.world.newExplosion(null, player.posX, player.posY, player.posZ, 12, true, true);

    player.attackEntityFrom(potato, player.getMaxHealth());
  }
  
  // These next two methods are from
  // https://github.com/CoFH/CoFHLib/blob/master/src/main/java/cofh/lib/util/helpers/EntityHelper.java
  // Two methods isn't justification for inclusion as a dependency, so I'm
  // opting to copy/paste
  //
  // Thanks skyboy!
  public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld)
  {
    WorldProvider pOld = oldWorld.provider;
    WorldProvider pNew = newWorld.provider;
    double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
    double x = entity.posX * moveFactor;
    double z = entity.posZ * moveFactor;
    oldWorld.theProfiler.startSection("placing");
    x = MathHelper.clamp_double(x, -29999872, 29999872);
    z = MathHelper.clamp_double(z, -29999872, 29999872);
    if (entity.isEntityAlive())
    {
      entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
      newWorld.spawnEntityInWorld(entity);
      newWorld.updateEntityWithOptionalForce(entity, false);
    }
    oldWorld.theProfiler.endSection();
    entity.setWorld(newWorld);
  }

  public static void transferPlayerToDimension(EntityPlayerMP player, double x, double y, double z, int dimension, PlayerList manager)
  {
    int oldDim = player.dimension;
    WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
    player.dimension = dimension;
    WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
    player.connection.sendPacket(new SPacketRespawn(player.dimension, player.world.getDifficulty(), player.world.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
    worldserver.removeEntityDangerously(player);
    if (player.isBeingRidden()) {
      player.removePassengers();
    }
    if (player.isRiding()) {
      player.dismountRidingEntity();
    }
    player.isDead = false;
    transferEntityToWorld(player, worldserver, worldserver1);
    manager.preparePlayer(player, worldserver);
    player.connection.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
    player.interactionManager.setWorld(worldserver1);
    manager.updateTimeAndWeatherForPlayer(player, worldserver1);
    manager.syncPlayerInventory(player);

    for (PotionEffect potioneffect : player.getActivePotionEffects()) {
      player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
    }
    FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
  }
}
