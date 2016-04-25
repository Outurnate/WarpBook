package com.panicnot42.warpbook.tileentity;

import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTeleporter extends TileEntity
{
  ItemStack page;
  
  public TileEntityTeleporter()
  {
  }

  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    page = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("page"));
  }
    
  @Override
  public void writeToNBT(NBTTagCompound tag)
  {
    super.writeToNBT(tag);
    NBTTagCompound pageTag = new NBTTagCompound();
    page.writeToNBT(pageTag);
    tag.setTag("page", pageTag);
  }

  public Waypoint GetWaypoint(EntityPlayer player)
  {
    return ((IDeclareWarp)page.getItem()).GetWaypoint(player, page);
  }

  public void SetPage(ItemStack stack)
  {
    page = stack;
    markDirty();
  }
}
