package com.panicnot42.warpbook;

import java.math.RoundingMode;
import java.util.Iterator;
import java.util.UUID;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.Waypoint;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class Proxy
{
  public void registerRenderers()
  {
  }

  public void handleWarp(EntityPlayer player, ItemStack page)
  {
    if (page == null) return;
    Waypoint wp = extractWaypoint(player, page);
    if (wp == null)
    {
      if (player.worldObj.isRemote && page.getItemDamage() != 5) CommandUtils.showError(player, I18n.format("help.waypointnotexist"));
      return;
    }
    /*
     * if (wp == null && wasTargetFound.get()) return; if (wp == null) { if
     * (player.worldObj.isRemote) CommandUtils.showError(player,
     * I18n.format(page.getItemDamage() == 2 ? "help.waypointnotexist" :
     * "help.selfaport")); return; // kind of important.... }
     */
    boolean crossDim = player.dimension != wp.dim;
    PacketEffect oldDim = new PacketEffect(true, MathUtils.round(player.posX, RoundingMode.DOWN), MathUtils.round(player.posY, RoundingMode.DOWN), MathUtils.round(player.posZ, RoundingMode.DOWN));
    PacketEffect newDim = new PacketEffect(false, wp.x, wp.y, wp.z);
    NetworkRegistry.TargetPoint oldPoint = new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 64);
    NetworkRegistry.TargetPoint newPoint = new NetworkRegistry.TargetPoint(wp.dim, wp.x, wp.y, wp.z, 64);
    player.addExhaustion(calculateExhaustion(player.getEntityWorld().difficultySetting, WarpBookMod.exhaustionCoefficient, crossDim));
    
    net.minecraft.world.Chunk chunk = player.worldObj.getChunkFromBlockCoords(wp.x, wp.z); // get the chunk from x and z coords
    if (!chunk.isChunkLoaded()) // will only run if that chunk is not already loaded
    {
      player.worldObj.getChunkProvider().loadChunk(chunk.xPosition, chunk.zPosition); // load the destination chunk
    }
    
    if (crossDim && !player.worldObj.isRemote) transferPlayerToDimension((EntityPlayerMP)player, wp.dim, ((EntityPlayerMP)player).mcServer.getConfigurationManager());
    player.setPositionAndUpdate(wp.x - 0.5f, wp.y + 0.5f, wp.z - 0.5f);
    if (!player.worldObj.isRemote)
    {
      WarpBookMod.network.sendToAllAround(oldDim, oldPoint);
      WarpBookMod.network.sendToAllAround(newDim, newPoint);
    }
  }

  protected Waypoint extractWaypoint(EntityPlayer player, ItemStack page)
  {
    NBTTagCompound pageTagCompound = page.getTagCompound();
    WarpWorldStorage storage = WarpWorldStorage.instance(player.getEntityWorld());
    Waypoint wp;
    if (pageTagCompound.hasKey("hypername"))
      wp = storage.getWaypoint(pageTagCompound.getString("hypername"));
    else if (pageTagCompound.hasKey("playeruuid") && PlayerUtils.isPlayerOnline(UUID.fromString(pageTagCompound.getString("playeruuid"))))
    {
      if (player.worldObj.isRemote) return null;
      EntityPlayer playerTo = PlayerUtils.getPlayerByUUID(UUID.fromString(pageTagCompound.getString("playeruuid")));
      if (player == playerTo) return null;
      wp = new Waypoint("", "", MathUtils.round(playerTo.posX, RoundingMode.DOWN), MathUtils.round(playerTo.posY, RoundingMode.DOWN), MathUtils.round(playerTo.posZ, RoundingMode.DOWN),
          playerTo.dimension);
    }
    else
      wp = new Waypoint("", "", pageTagCompound.getInteger("posX"), pageTagCompound.getInteger("posY"), pageTagCompound.getInteger("posZ"), pageTagCompound.getInteger("dim"));
    return wp;
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

  @SubscribeEvent
  public void onHurt(LivingHurtEvent event)
  {
    if (WarpBookMod.deathPagesEnabled && event.entity instanceof EntityPlayer)
    {
      EntityPlayer player = (EntityPlayer)event.entity;
      if (event.source != DamageSource.outOfWorld && player.getHealth() <= event.ammount) for (ItemStack item : player.inventory.mainInventory)
        if (item != null && item.getItem() instanceof WarpBookItem && WarpBookItem.getRespawnsLeft(item) > 0)
        {
          WarpBookItem.decrRespawnsLeft(item);
          WarpWorldStorage.instance(player.worldObj).setLastDeath(player.getGameProfile().getId(), player.posX, player.posY, player.posZ, player.dimension);
          break;
        }
    }
  }

  @SubscribeEvent
  public void onPlayerRespawn(PlayerRespawnEvent event)
  {
    if (WarpBookMod.deathPagesEnabled)
    {
      ItemStack page = new ItemStack(WarpBookMod.warpPageItem, 1);
      Waypoint death = WarpWorldStorage.getLastDeath(event.player.getGameProfile().getId());
      if (death != null) WarpPageItem.writeWaypointToPage(page, WarpWorldStorage.getLastDeath(event.player.getGameProfile().getId()));
      event.player.inventory.addItemStackToInventory(page);
      WarpWorldStorage.instance(event.player.worldObj).clearLastDeath(event.player.getGameProfile().getId());
    }
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
    player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager
        .getGameType()));
    worldserver.removePlayerEntityDangerously(player);
    player.isDead = false;
    transferEntityToWorld(player, worldserver, worldserver1);
    manager.func_72375_a(player, worldserver);
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
