package com.panicnot42.warpbook.core;

import java.math.RoundingMode;
import java.util.Iterator;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
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
      if (page == null) return;
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
      player.addExhaustion(calculateExhaustion(player.getEntityWorld().getDifficulty(), WarpBookMod.exhaustionCoefficient, crossDim));
      
      if (crossDim && !player.worldObj.isRemote) transferPlayerToDimension((EntityPlayerMP)player, wp.dim, ((EntityPlayerMP)player).mcServer.getConfigurationManager());
      player.setPositionAndUpdate(wp.x - 0.5f, wp.y + 0.5f, wp.z - 0.5f);
      //if (!player.worldObj.isRemote)
      //{
        WarpBookMod.network.sendToAllAround(oldDim, oldPoint);
        WarpBookMod.network.sendToAllAround(newDim, newPoint);
      //}
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

  @SuppressWarnings("unchecked")
  public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager manager)
  {
    int oldDim = player.dimension;
    WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
    player.dimension = dimension;
    WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
    player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager
        .getGameType()));
    worldserver.removePlayerEntityDangerously(player);
    player.isDead = false;
    transferEntityToWorld(player, worldserver, worldserver1);
    manager.preparePlayer(player, worldserver);
    player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
    player.theItemInWorldManager.setWorld(worldserver1);
    manager.updateTimeAndWeatherForPlayer(player, worldserver1);
    manager.syncPlayerInventory(player);
    Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
    while (iterator.hasNext())
    {
      PotionEffect potioneffect = iterator.next();
      player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
    }
    FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
  }
}
