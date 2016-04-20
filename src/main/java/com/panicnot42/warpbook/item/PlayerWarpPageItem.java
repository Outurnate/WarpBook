package com.panicnot42.warpbook.item;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerWarpPageItem extends Item implements IDeclareWarp
{
  public PlayerWarpPageItem()
  {
    super.setMaxStackSize(16).setUnlocalizedName("playerwarppage");
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public String GetName(World world, ItemStack stack)
  {
    return stack.getTagCompound().getString("player");
  }

  @Override
  public Waypoint GetWaypoint(EntityPlayer player, ItemStack stack)
  {
    WarpWorldStorage storage = WarpWorldStorage.instance(player.getEntityWorld());
    Waypoint wp;
    if (player.worldObj.isRemote)
      return null;
    UUID playerID = UUID.fromString(stack.getTagCompound().getString("playeruuid"));
    EntityPlayerMP playerTo = null;
    List<EntityPlayerMP> allPlayers = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    for (EntityPlayerMP playerS : allPlayers)
      if (playerS.getUniqueID().equals(playerID))
        playerTo = playerS;
    if (player == playerTo || playerTo == null)
      return null;
    return new Waypoint("", "",
                        MathUtils.round(playerTo.posX, RoundingMode.DOWN),
                        MathUtils.round(playerTo.posY, RoundingMode.DOWN),
                        MathUtils.round(playerTo.posZ, RoundingMode.DOWN),
                        playerTo.dimension);
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    if (player.isSneaking())
    {
      itemStack.setItemDamage(0);
      itemStack.setTagCompound(new NBTTagCompound());
    }
    else
    {
      WarpBookMod.warpDrive.handleWarp(player, itemStack);
      if (!player.capabilities.isCreativeMode)
        --itemStack.stackSize;
    }
    return itemStack;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    if (item.hasTagCompound())
    {
      list.add(GetName(player.worldObj, item));
    }
  }
  
  public Boolean ValidData(ItemStack stack)
  {
    return true;
  }
}
