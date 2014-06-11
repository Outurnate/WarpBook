package com.panicnot42.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.EventListenerList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

public class SyncableTable<T extends INBTSerializable> implements INBTSerializable // TODO:
// is
// this
// threadsafe?
// research
// netty/forge
// communication
{
  private class TablePacket extends AbstractPacket
  {
    private HashMap<String, T> payload;

    private TablePacket(HashMap<String, T> payload)
    {
      this.payload = payload;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
      NBTTagCompound tag = new NBTTagCompound();
      for (Entry<String, T> e : this.payload.entrySet())
      {
        NBTTagCompound entry = new NBTTagCompound();
        e.getValue().writeToNBT(entry);
        tag.setTag(e.getKey(), entry);
      }
      ByteBufUtils.writeTag(buffer, tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
      NBTTagCompound tag = ByteBufUtils.readTag(buffer);
      for (String e : (Set<String>)tag.func_150296_c())
      {
        NBTTagCompound entry = tag.getCompoundTag(e);
        T obj;
        try
        {
          obj = clazz.newInstance();
        }
        catch (InstantiationException e1)
        {
          throw new RuntimeException(e1);
        }
        catch (IllegalAccessException e1)
        {
          throw new RuntimeException(e1);
        }
        obj.readFromNBT(entry);
        this.payload.put(e, obj);
      }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
      copyParent();
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
      copyParent();
    }

    private void copyParent()
    {
      table = new HashMap<String, T>(payload); // here's where I may need sync
      dirty = false;
    }
  }

  private PacketPipeline pipeline;
  private HashMap<String, T> table;
  private Class<T> clazz;
  private boolean dirty = false;
  private EventListenerList updateTableListeners = new EventListenerList();
  private String rootTagName;

  public SyncableTable(PacketPipeline pipeline, Class<T> clazz, String rootTagName)
  {
    this.pipeline = pipeline;
    this.clazz = clazz;
    this.table = new HashMap<String, T>();
    this.rootTagName = rootTagName;
    FMLCommonHandler.instance().bus().register(this);
  }

  public void set(String key, T value)
  {
    table.put(key, value);
    markDirty();
  }

  public T get(String key)
  {
    return table.get(key);
  }

  public T remove(String waypoint)
  {
    return table.remove(waypoint);
  }

  public void addUpdateTableListener(UpdateTableListener listener)
  {
    updateTableListeners.add(UpdateTableListener.class, listener);
  }

  public void removeUpdateTableListener(UpdateTableListener listener)
  {
    updateTableListeners.remove(UpdateTableListener.class, listener);
  }

  protected void fireUpdate(UpdateTableEvent updateTableEvent)
  {
    for (UpdateTableListener listener : updateTableListeners.getListeners(UpdateTableListener.class))
      listener.tableUpdated(updateTableEvent);
  }

  @EventHandler
  public void registerHandlers(FMLPostInitializationEvent event)
  {
    pipeline.registerPacket(TablePacket.class);
  }

  @SubscribeEvent
  public void tick(TickEvent e)
  {
    if (dirty) clean();
  }

  @SubscribeEvent
  public void clientJoined(ServerConnectionFromClientEvent e)
  {
    pipeline.sendTo(new TablePacket(table), ((NetHandlerPlayServer)e.handler).playerEntity);
  }

  private void markDirty()
  {
    dirty = true;
  }

  private void clean()
  {
    switch (FMLCommonHandler.instance().getEffectiveSide())
    {
      case CLIENT:
        pipeline.sendToServer(new TablePacket(table));
        break;
      case SERVER:
        pipeline.sendToAll(new TablePacket(table));
        break;
      default:
        break;
    }
    dirty = false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void readFromNBT(NBTTagCompound var1)
  {
    NBTUtils.readHashMapFromNBT(var1.getTagList(rootTagName, new NBTTagCompound().getId()), (Class<INBTSerializable>)clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTUtils.writeHashMapToNBT(var1.getTagList(rootTagName, new NBTTagCompound().getId()), (HashMap<String, INBTSerializable>)table);
  }

  public boolean contains(String name)
  {
    return table.keySet().contains(name);
  }

  public String[] keyList()
  {
    String[] keySet = new String[table.size()];
    table.keySet().toArray(keySet);
    return keySet;
  }
}
