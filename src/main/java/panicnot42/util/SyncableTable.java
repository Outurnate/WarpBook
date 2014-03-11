package panicnot42.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.EventListenerList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SyncableTable<T extends INBTSerializable> // TODO: is this threadsafe?  research netty/forge communication
{
  private class TablePacket extends AbstractPacket
  {
    private HashMap<String, T> payload;
    
    public TablePacket() { }
    private TablePacket(HashMap<String, T> payload) { this.payload = payload; }
    
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
  
  public SyncableTable(PacketPipeline pipeline, Class<T> clazz)
  {
    this.pipeline = pipeline;
    this.clazz = clazz;
    FMLCommonHandler.instance().bus().register(this); 
  }
  
  public void set(String key, T value)
  {
    table.put(key, value);
    markDirty();
  }

  public T set(String key)
  {
    return table.get(key);
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
    {
      listener.tableUpdated(updateTableEvent);
    }
  }
  
  @SubscribeEvent
  public void registerHandlers(FMLPostInitializationEvent event)
  {
    pipeline.registerPacket(TablePacket.class);
  }
  
  @SubscribeEvent
  public void tick(TickEvent e)
  {
    if (dirty)
      clean();
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
}
