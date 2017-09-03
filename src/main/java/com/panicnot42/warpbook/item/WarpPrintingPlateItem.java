package com.panicnot42.warpbook.item;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class WarpPrintingPlateItem extends Item
{
  public WarpPrintingPlateItem(String name)
  {
    setMaxStackSize(64);
    setUnlocalizedName(name);
    setRegistryName(name);
  }

  public static ItemStack print(ItemStack book)
  {
    ItemStack stack = new ItemStack(WarpBookMod.items.warpPrintingPlateItem, 1);
    NBTTagList pages = book.getTagCompound().getTagList("WarpPages", Constants.NBT.TAG_COMPOUND);
    NBTTagList destPages = new NBTTagList();
    for (int i = 0; i < pages.tagCount(); ++i)
    {
      ItemStack item = new ItemStack(pages.getCompoundTagAt(i));
      if (item.getItem() instanceof IDeclareWarp && ((IDeclareWarp)item.getItem()).WarpCloneable())
      {
        NBTTagCompound tag = new NBTTagCompound();
        item.writeToNBT(tag);
        destPages.appendTag(tag);
      }
    }
    stack.setTagCompound(new NBTTagCompound());
    stack.getTagCompound().setTag("WarpPages", destPages);
    return stack;
  }
}
