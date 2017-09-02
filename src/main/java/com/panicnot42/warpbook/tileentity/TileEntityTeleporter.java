package com.panicnot42.warpbook.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTeleporter extends TileEntity
{
  ItemStack page;
  
  public TileEntityTeleporter()
  {
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket()
  {
    NBTTagCompound syncData = new NBTTagCompound();
    this.write(syncData);
    return new SPacketUpdateTileEntity(this.pos, 1, syncData);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
  {
    read(pkt.getNbtCompound());
  }
  
  @Override
  public void readFromNBT(NBTTagCompound tag)
  {
    super.readFromNBT(tag);
    read(tag);
  }
    
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag)
  {
    super.writeToNBT(tag);
    write(tag);
    return tag;
  }

  private void read(NBTTagCompound tag)
  {
    page = new ItemStack(tag.getCompoundTag("page"));
  }

  private void write(NBTTagCompound tag)
  {
    if (page != null)
    {
      NBTTagCompound pageTag = new NBTTagCompound();
      page.writeToNBT(pageTag);
      tag.setTag("page", pageTag);
    }
  }

  public ItemStack GetPage()
  {
    return page;
  }

  public void SetPage(ItemStack stack)
  {
    page = stack;
    markDirty();
  }

  @Override
  public boolean shouldRefresh(World w, BlockPos pos, IBlockState oldState, IBlockState newState)
  {
    return oldState.getBlock() != newState.getBlock();
  }
}
