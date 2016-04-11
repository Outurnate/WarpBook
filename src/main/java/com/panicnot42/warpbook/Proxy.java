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

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class Proxy
{
  public void registerRenderers()
  {
  }

  public void registerModels()
  {
  }
  
  public void postInit()
  {
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
//      ItemStack page = new ItemStack(WarpBookMod.warpPageItem, 1);
      Waypoint death = WarpWorldStorage.getLastDeath(event.player.getGameProfile().getId());
      if (death != null)
      {
//        WarpPageItem.writeWaypointToPage(page, WarpWorldStorage.getLastDeath(event.player.getGameProfile().getId()));
//        event.player.inventory.addItemStackToInventory(page);
        WarpWorldStorage.instance(event.player.worldObj).clearLastDeath(event.player.getGameProfile().getId());
      }
    }
  }
}
