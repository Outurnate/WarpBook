package com.panicnot42.warpbook.util.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.EventListenerList;

import com.panicnot42.warpbook.Properties;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.util.nbt.INBTSerializable;
import com.panicnot42.warpbook.util.nbt.NBTUtils;
import com.panicnot42.warpbook.util.net.SyncableTable.TablePacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.util.Constants;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class SyncableTable<T extends INBTSerializable> implements INBTSerializable, IMessageHandler<SyncableTable<T>.TablePacket, IMessage>
{
  public class TablePacket implements IMessage
  {
    private HashMap<String, T> payload;

    private TablePacket(HashMap<String, T> payload)
    {
      this.payload = payload;
    }

    @Override
    public void fromBytes(ByteBuf buffer)
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
    public void toBytes(ByteBuf buffer)
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
  }

  private SimpleNetworkWrapper pipeline;
  private HashMap<String, T> table;
  private Class<T> clazz;
  private boolean dirty = false;
  private String rootTagName;
  private static int disc = 1;

  public SyncableTable(Class<T> clazz, String rootTagName)
  {
    this.pipeline = NetworkRegistry.INSTANCE.newSimpleChannel(rootTagName);
    this.pipeline.registerMessage(SyncableTable.class, TablePacket.class, disc++, Side.CLIENT);
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
    NBTUtils.readHashMapFromNBT(var1.getTagList(rootTagName, Constants.NBT.TAG_COMPOUND), (Class<INBTSerializable>)clazz);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void writeToNBT(NBTTagCompound var1)
  {
    NBTUtils.writeHashMapToNBT(var1.getTagList(rootTagName, Constants.NBT.TAG_COMPOUND), (HashMap<String, INBTSerializable>)table);
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

  private void copyParent(TablePacket message)
  {
    table = new HashMap<String, T>(message.payload);
    dirty = false;
  }

  @Override
  public IMessage onMessage(TablePacket message, MessageContext ctx)
  {
    copyParent(message);
    return null;
  }
}
