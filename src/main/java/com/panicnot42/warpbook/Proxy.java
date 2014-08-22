package com.panicnot42.warpbook;

import java.math.RoundingMode;
import java.util.UUID;

import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;

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
      CommandUtils.showError(player, "This waypoint no longer exists");
      return; // kind of important....
    }
    boolean crossDim = player.dimension != wp.dim;
    player.addExhaustion(calculateExhaustion(player.getEntityWorld().difficultySetting, WarpBookMod.exhaustionCoefficient, crossDim));
    if (crossDim)
      ((EntityPlayerMP)player).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)player, wp.dim, new WarpBookTeleporter(((EntityPlayerMP)player).mcServer.worldServerForDimension(wp.dim)));
    player.setPositionAndUpdate(wp.x + 0.5f, wp.y + 0.5f, wp.z + 0.5f);
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
      EntityPlayer playerTo = PlayerUtils.getPlayerByUUID(UUID.fromString(pageTagCompound.getString("playeruuid")));
      wp = (playerTo != player) ? new Waypoint("", "", MathUtils.round(playerTo.posX, RoundingMode.DOWN), MathUtils.round(playerTo.posY, RoundingMode.DOWN), MathUtils.round(playerTo.posZ, RoundingMode.DOWN), playerTo.dimension) : null;
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
}
