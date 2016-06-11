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

  public WarpPageItem(String name)
  {
    super.setHasSubtypes(true).setMaxStackSize(16).setMaxDamage(0).setUnlocalizedName(name);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    ItemStack newStack;
    switch (itemStack.getItemDamage())
    {
    default:
    case 0:
      newStack = new ItemStack(WarpBookMod.items.unboundWarpPageItem, itemStack.stackSize);
      break;
    case 1:
      newStack = new ItemStack(WarpBookMod.items.boundWarpPageItem, itemStack.stackSize);
      break;
    case 2:
      newStack = new ItemStack(WarpBookMod.items.hyperWarpPageItem, itemStack.stackSize);
      break;
    case 3:
      newStack = new ItemStack(WarpBookMod.items.deathlyWarpPageItem, itemStack.stackSize);
      break;
    case 4:
      newStack = new ItemStack(WarpBookMod.items.potatoWarpPageItem, itemStack.stackSize);
      break;
    case 5:
      newStack = new ItemStack(WarpBookMod.items.playerWarpPageItem, itemStack.stackSize);
      break;
    }
    if (itemStack.hasTagCompound())
      newStack.setTagCompound(itemStack.getTagCompound());
    return newStack;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    list.add(I18n.format("help.legacy"));
  }
}
