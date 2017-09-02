package com.panicnot42.warpbook.item;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerWarpPageItem extends Item implements IDeclareWarp
{
  public PlayerWarpPageItem(String name)
  {
    super.setMaxStackSize(16).setUnlocalizedName(name);
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
    WarpWorldStorage storage = WarpWorldStorage.get(player.getEntityWorld());
    Waypoint wp;
    if (player.world.isRemote)
      return null;
    UUID playerID = UUID.fromString(stack.getTagCompound().getString("playeruuid"));
    EntityPlayerMP playerTo = null;
    List<EntityPlayerMP> allPlayers = player.getServer().getPlayerList().getPlayers();
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
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn)
  {
    ItemStack item = player.getHeldItem(handIn);
    if (player.isSneaking())
    {
      item = new ItemStack(WarpBookMod.items.unboundWarpPageItem, item.getCount());
    }
    else
    {
      if (player.getUniqueID().compareTo(UUID.fromString(item.getTagCompound().getString("playeruuid"))) == 0)
      {
        if (!worldIn.isRemote)
          CommandUtils.showError(player, I18n.format("help.selfaport"));
      }
      else
        WarpBookMod.warpDrive.handleWarp(player, item);
      
      if (!player.capabilities.isCreativeMode)
      {
        item = new ItemStack(item.getItem(), item.getCount() - 1);
        item.setTagCompound(item.getTagCompound());
      }
    }
    
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    if (item.hasTagCompound())
    {
      list.add(GetName(player.world, item));
    }
  }

  @Override
  public Boolean ValidData(ItemStack stack)
  {
    return stack.getTagCompound().hasKey("playeruuid");
  }
  
  @Override
  public Boolean WarpCloneable()
  {
    return true;
  }
}
