package com.panicnot42.warpbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.panicnot42.warpbook.commands.CreateWaypointCommand;
import com.panicnot42.warpbook.commands.DeleteWaypointCommand;
import com.panicnot42.warpbook.commands.GiveWarpCommand;
import com.panicnot42.warpbook.commands.ListWaypointCommand;
import com.panicnot42.warpbook.crafting.WarpBookShapeless;
import com.panicnot42.warpbook.crafting.WarpPageShapeless;
import com.panicnot42.warpbook.gui.GuiManager;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.net.packet.PacketEffect;
import com.panicnot42.warpbook.net.packet.PacketSyncPlayers;
import com.panicnot42.warpbook.net.packet.PacketSyncWaypoints;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.net.packet.PacketWaypointName;
import com.panicnot42.warpbook.util.PlayerUtils;


@Mod(modid = Properties.modid, name = Properties.name, version = Properties.version)
public class WarpBookMod
{
  @Mod.Instance(value = Properties.modid)
  public static WarpBookMod instance;

  public static final Logger logger = LogManager.getLogger(Properties.modid);
  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Properties.modid);

  public static WarpBookItem warpBookItem;
  public static WarpPageItem warpPageItem;

  @SidedProxy(clientSide = "com.panicnot42.warpbook.client.ClientProxy", serverSide = "com.panicnot42.warpbook.Proxy")
  public static Proxy proxy;

  private static int guiIndex = 42;

  public static float exhaustionCoefficient;
  public static boolean deathPagesEnabled = true;
  public static boolean fuelEnabled = false;

  public static final int WarpBookWarpGuiIndex = guiIndex++;
  public static final int WarpBookWaypointGuiIndex = guiIndex++;
  public static final int WarpBookInventoryGuiIndex = guiIndex++;

  public static HashMap<EntityPlayer, ItemStack> lastHeldBooks = new HashMap<EntityPlayer, ItemStack>();
  public static HashMap<EntityPlayer, ItemStack> formingPages = new HashMap<EntityPlayer, ItemStack>();

  public static CreativeTabs tabBook = new CreativeTabs("tabWarpBook")
  {
    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
      return warpBookItem;
    }
  };

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    exhaustionCoefficient = (float)config.get("tweaks", "exhaustion coefficient", 10.0f).getDouble(10.0);
    deathPagesEnabled = config.get("features", "death pages", true).getBoolean(true);
    fuelEnabled = config.get("features", "fuel", false).getBoolean(false);
    warpBookItem = new WarpBookItem();
    warpPageItem = new WarpPageItem();
    proxy.registerModels();
    GameRegistry.registerItem(warpBookItem, "warpbook");
    GameRegistry.registerItem(warpPageItem, "warppage");
    List<ItemStack> bookRecipe = new ArrayList<ItemStack>();
    if (config.get("tweaks", "hard recipes", false).getBoolean(false))
    {
      bookRecipe.add(new ItemStack(Items.book));
      bookRecipe.add(new ItemStack(Items.nether_star));
      GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new ItemStack(Items.paper), new ItemStack(Items.ender_eye));
    }
    else
    {
      bookRecipe.add(new ItemStack(Items.book));
      bookRecipe.add(new ItemStack(Items.ender_pearl));
      GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new ItemStack(Items.paper), new ItemStack(Items.ender_pearl));
    }
    ItemStack emptyBook = new ItemStack(warpBookItem);
    ItemStack boundpage = new ItemStack(warpPageItem, 1, 1);
    List<ItemStack> recipe = new ArrayList<ItemStack>();
    recipe.add(boundpage);
    recipe.add(new ItemStack(warpPageItem));
    GameRegistry.addRecipe(new WarpPageShapeless(boundpage, recipe));
    GameRegistry.addRecipe(new WarpBookShapeless(emptyBook, bookRecipe));
    if (deathPagesEnabled)
      GameRegistry.addShapedRecipe(new ItemStack(warpPageItem, 1, 3), " x ", "yzy", "   ", 'z', new ItemStack(warpPageItem, 1), 'y', new ItemStack(Items.diamond), 'x', new ItemStack(
          Items.fermented_spider_eye));
    GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem, 1, 5), new ItemStack(warpPageItem, 1), new ItemStack(Items.potato));

    config.save();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    proxy.registerRenderers();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    int disc = 0;
    WarpWorldStorage.postInit();
    network.registerMessage(PacketWarp.class, PacketWarp.class, disc++, Side.SERVER);
    network.registerMessage(PacketWaypointName.class, PacketWaypointName.class, disc++, Side.SERVER);
    network.registerMessage(PacketSyncWaypoints.class, PacketSyncWaypoints.class, disc++, Side.CLIENT);
    network.registerMessage(PacketSyncPlayers.class, PacketSyncPlayers.class, disc++, Side.CLIENT);
    network.registerMessage(PacketEffect.class, PacketEffect.class, disc++, Side.CLIENT);
    MinecraftForge.EVENT_BUS.register(proxy);
    MinecraftForge.EVENT_BUS.register(this);
    FMLCommonHandler.instance().bus().register(proxy);
    FMLCommonHandler.instance().bus().register(this);

    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(warpBookItem, 0, new ModelResourceLocation(Properties.modid.toLowerCase() + ":warpbook", "inventory"));
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(warpPageItem, 0, new ModelResourceLocation(Properties.modid.toLowerCase() + ":warppage", "inventory"));
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
  public void clientJoined(FMLNetworkEvent.ServerConnectionFromClientEvent e)
  {
    EntityPlayerMP player = ((NetHandlerPlayServer)e.handler).playerEntity;
    if (!player.worldObj.isRemote)
    {
      WarpWorldStorage.instance(player.worldObj).updateClient(player, e);
      PlayerUtils.instance().updateClient(player, e);
    }
  }

  @SubscribeEvent
  public void clientLeft(FMLNetworkEvent.ServerDisconnectionFromClientEvent e)
  {
    EntityPlayerMP player = ((NetHandlerPlayServer)e.handler).playerEntity;
    PlayerUtils.instance().removeClient(player);
  }
}
