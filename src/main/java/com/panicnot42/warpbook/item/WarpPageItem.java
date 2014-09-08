package com.panicnot42.warpbook.item;

import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.WarpWorldStorage;
import com.panicnot42.warpbook.util.MathUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.Waypoint;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WarpPageItem extends Item
{
  private static final String[] itemMetaNames = new String[] { "unbound", "bound", "hyperbound", "deathly", "potato", "player" };
  private static final String[] itemTextures = new String[] { "unboundwarppage", "boundwarppage", "hyperboundwarppage", "deathlywarppage", "spudpage", "playerpage" };

  @SideOnly(Side.CLIENT)
  private IIcon[] itemIcons;

  public WarpPageItem()
  {
    super.setHasSubtypes(true).setMaxStackSize(16).setCreativeTab(WarpBookMod.tabBook).setMaxDamage(0).setUnlocalizedName("warppage");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IIcon getIconFromDamage(int meta)
  {
    return itemIcons[MathHelper.clamp_int(meta, 0, itemMetaNames.length - 1)];
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack)
  {
    return super.getUnlocalizedName() + "." + itemMetaNames[MathHelper.clamp_int(itemStack.getItemDamage(), 0, itemMetaNames.length - 1)];
  }

  @Override
  public void registerIcons(IIconRegister iconRegister)
  {
    itemIcons = new IIcon[itemTextures.length];
    for (int i = 0; i < itemTextures.length; ++i)
      itemIcons[i] = iconRegister.registerIcon("warpbook:" + itemTextures[i]);
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
        case 0:
          itemStack.setItemDamage(5);
          itemStack.setTagCompound(new NBTTagCompound());
          if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) itemStack.getTagCompound().setString("playeruuid", player.getGameProfile().getId().toString());
          break;
        case 1:
        case 3:
        case 4:
        case 5:
          itemStack.setItemDamage(0);
          itemStack.setTagCompound(new NBTTagCompound());
          break;
        case 2: // do not clear
          break;
      }
    }
    else
    {
      switch (itemStack.getItemDamage())
      {
        case 0:
          ItemStack newStack = new ItemStack(WarpBookMod.warpPageItem, 1, 1);
          writeWaypointToPage(newStack, MathUtils.round(player.posX, RoundingMode.HALF_DOWN), MathUtils.round(player.posY, RoundingMode.HALF_DOWN),
              MathUtils.round(player.posZ, RoundingMode.HALF_DOWN), player.dimension);
          player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWaypointGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
          WarpBookMod.formingPages.put(player, newStack);
          break;
        case 1:
        case 2:
        case 5:
          WarpBookMod.proxy.handleWarp(player, itemStack);
          if (!player.capabilities.isCreativeMode) --itemStack.stackSize;
          break;
        case 3: // do nothing
          break;
        case 4:
          itemStack = new ItemStack(GameRegistry.findItem("minecraft", "poisonous_potato"), 1);
          WarpBookMod.proxy.goFullPotato(player, itemStack);
          break;
      }
    }
    return itemStack;
  }

  public static void writeWaypointToPage(ItemStack page, int x, int y, int z, int dim)
  {
    page.setItemDamage(1);
    if (!page.hasTagCompound()) page.setTagCompound(new NBTTagCompound());
    page.getTagCompound().setInteger("posX", x);
    page.getTagCompound().setInteger("posY", y);
    page.getTagCompound().setInteger("posZ", z);
    page.getTagCompound().setInteger("dim", dim);
  }

  public static void writeWaypointToPage(ItemStack page, Waypoint wp)
  {
    writeWaypointToPage(page, wp.x, wp.y, wp.z, wp.dim);
    page.getTagCompound().setString("name", wp.friendlyName);
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    switch (item.getItemDamage())
    {
      case 1:
        try
        {
          list.add(item.getTagCompound().getString("name"));
          list.add(I18n.format("warpbook.bindmsg", item.getTagCompound().getInteger("posX"), item.getTagCompound().getInteger("posY"), item.getTagCompound().getInteger("posZ"), item.getTagCompound()
              .getInteger("dim")));
        }
        catch (Exception e)
        {
          // gui hasn't closed
        }
        break;
      case 2:
        String name = item.getTagCompound().getString("hypername");
        list.add(name);
        list.add(WarpWorldStorage.instance(player.worldObj).getWaypoint(name).friendlyName);
        break;
      case 3:
        break;
      case 4:
        list.add(I18n.format("help.potato.flavour1"));
        list.add(I18n.format("help.potato.flavour2"));
        break;
      case 5:
        list.add(PlayerUtils.getNameByUUID(UUID.fromString(item.getTagCompound().getString("playeruuid"))));
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
