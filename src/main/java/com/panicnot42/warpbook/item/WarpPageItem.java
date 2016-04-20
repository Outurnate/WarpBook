package com.panicnot42.warpbook.item;

import java.util.List;
import java.util.UUID;

import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WarpPageItem extends Item
{
  private static final String[] itemMetaNames = new String[] { "unbound", "bound", "hyperbound", "deathly", "potato", "player" };

  public WarpPageItem()
  {
    super.setHasSubtypes(true).setMaxStackSize(16).setCreativeTab(WarpBookMod.tabBook).setMaxDamage(0).setUnlocalizedName("warppage");
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack)
  {
    return super.getUnlocalizedName() + "." + itemMetaNames[MathHelper.clamp_int(itemStack.getItemDamage(), 0, itemMetaNames.length - 1)];
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    if (player.isSneaking())
    {
      switch (itemStack.getItemDamage())
      {
        case 3:
        case 4:
          break;
      }
    }
    else
    {
      switch (itemStack.getItemDamage())
      {
        case 3: // do nothing
          break;
        case 4:
          itemStack = new ItemStack(GameRegistry.findItem("minecraft", "poisonous_potato"), 1);
          WarpBookMod.warpDrive.goFullPotato(player, itemStack);
          break;
      }
    }
    return itemStack;
  }

/*
  public static void writeWaypointToPage(ItemStack page, Waypoint wp)
  {
    writeWaypointToPage(page, wp.x, wp.y, wp.z, wp.dim);
    page.getTagCompound().setString("name", wp.friendlyName);
    }*/

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    switch (item.getItemDamage())
    {
      case 3:
        break;
      case 4:
        list.add(I18n.format("help.potato.flavour1"));
        list.add(I18n.format("help.potato.flavour2"));
        break;
      case 5:
        //list.add(PlayerUtils.getNameByUUID(UUID.fromString(item.getTagCompound().getString("playeruuid"))));
        break;
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tab, List items)
  {
    items.add(new ItemStack(item, 1, 0));
    items.add(new ItemStack(item, 1, 3));
    items.add(new ItemStack(item, 1, 4));
  }

  @Override
  public boolean hasContainerItem(ItemStack itemStack)
  {
    return itemStack.getItemDamage() == 1;
  }

  @Override
  public ItemStack getContainerItem(ItemStack itemStack)
  {
    return itemStack.getItemDamage() == 1 ? itemStack.copy() : null;
  }
}
