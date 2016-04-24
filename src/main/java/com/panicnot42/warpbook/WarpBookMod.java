package com.panicnot42.warpbook;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.panicnot42.warpbook.block.TileEntityBookClonerBlock;
import com.panicnot42.warpbook.commands.CreateWaypointCommand;
import com.panicnot42.warpbook.commands.DeleteWaypointCommand;
import com.panicnot42.warpbook.commands.GiveWarpCommand;
import com.panicnot42.warpbook.commands.ListWaypointCommand;
import com.panicnot42.warpbook.core.WarpDrive;
import com.panicnot42.warpbook.gui.GuiManager;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.net.packet.PacketWaypointName;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;
import com.panicnot42.warpbook.util.Waypoint;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Properties.modid, name = Properties.name, version = Properties.version)
public class WarpBookMod
{
  @Mod.Instance(value = Properties.modid)
  public static WarpBookMod instance;

  public static final Logger logger = LogManager.getLogger(Properties.modid);
  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Properties.modid);

  @SidedProxy(clientSide = "com.panicnot42.warpbook.client.ClientProxy", serverSide = "com.panicnot42.warpbook.Proxy")
  public static Proxy proxy;

  public static WarpDrive warpDrive = new WarpDrive();
  public static WarpItems items;

  private static int guiIndex = 42;

  public static float exhaustionCoefficient;
  public static boolean deathPagesEnabled = true;
  public static boolean fuelEnabled = false;

  public static TileEntityBookClonerBlock bookCloner;

  public static final int WarpBookWarpGuiIndex = guiIndex++;
  public static final int WarpBookWaypointGuiIndex = guiIndex++;
  public static final int WarpBookInventoryGuiIndex = guiIndex++;
  public static final int BookClonerInventoryGuiIndex = guiIndex++;

  public static HashMap<EntityPlayer, ItemStack> lastHeldBooks = new HashMap<EntityPlayer, ItemStack>();
  public static HashMap<EntityPlayer, ItemStack> formingPages = new HashMap<EntityPlayer, ItemStack>();

  private static Configuration config;

  public static CreativeTabs tabBook = new CreativeTabs("tabWarpBook")
  {
    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
      return items.warpBookItem;
    }
  };

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    
    exhaustionCoefficient = (float)config.get("tweaks", "exhaustion coefficient", 10.0f).getDouble(10.0);
    deathPagesEnabled = config.get("features", "death pages", true).getBoolean(true);
    fuelEnabled = config.get("features", "fuel", false).getBoolean(false);
    
    items = new WarpItems();
    bookCloner = new TileEntityBookClonerBlock();

    config.save();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
    items.Register();
    GameRegistry.registerTileEntity(TileEntityBookCloner.class, "tileEntityBookCloner");
    GameRegistry.registerBlock(bookCloner, "bookcloner");
    proxy.registerRenderers();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    int disc = 0;
    WarpWorldStorage.postInit();
    network.registerMessage(PacketWarp.class, PacketWarp.class, disc++, Side.SERVER);
    network.registerMessage(PacketWaypointName.class, PacketWaypointName.class, disc++, Side.SERVER);
    network.registerMessage(PacketSyncWaypoints.class, PacketSyncWaypoints.class, disc++, Side.CLIENT);
    network.registerMessage(PacketEffect.class, PacketEffect.class, disc++, Side.CLIENT);
    MinecraftForge.EVENT_BUS.register(proxy);
    MinecraftForge.EVENT_BUS.register(this);
    FMLCommonHandler.instance().bus().register(proxy);
    FMLCommonHandler.instance().bus().register(this);
  }

  @Mod.EventHandler
  public void serverStarting(FMLServerStartingEvent event)
  {
    ServerCommandManager manager = ((ServerCommandManager)MinecraftServer.getServer().getCommandManager());
    manager.registerCommand(new CreateWaypointCommand());
    manager.registerCommand(new ListWaypointCommand());
    manager.registerCommand(new DeleteWaypointCommand());
    manager.registerCommand(new GiveWarpCommand());
  }

  @SubscribeEvent
  public void onHurt(LivingHurtEvent event)
  {
    if (WarpBookMod.deathPagesEnabled && event.entity instanceof EntityPlayer)
    {
      EntityPlayer player = (EntityPlayer)event.entity;
      if (event.source != DamageSource.outOfWorld && player.getHealth() <= event.ammount) for (ItemStack item : player.inventory.mainInventory)
        if (item != null && item.getItem() instanceof WarpBookItem && WarpBookItem.getRespawnsLeft(item) > 0)
        {
          WarpBookItem.decrRespawnsLeft(item);
          WarpWorldStorage.instance(player.worldObj).setLastDeath(player.getGameProfile().getId(), player.posX, player.posY, player.posZ, player.dimension);
          break;
        }
    }
  }

  @SubscribeEvent
  public void onPlayerRespawn(PlayerRespawnEvent event)
  {
    if (WarpBookMod.deathPagesEnabled)
    {
      Waypoint death = WarpWorldStorage.getLastDeath(event.player.getGameProfile().getId());
      if (death != null)
      {
        WarpWorldStorage.instance(event.player.worldObj).clearLastDeath(event.player.getGameProfile().getId());
      }
    }
  }
}
