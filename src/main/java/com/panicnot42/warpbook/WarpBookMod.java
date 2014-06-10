package com.panicnot42.warpbook;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import javax.security.auth.login.Configuration;

import com.panicnot42.util.PacketPipeline;
import com.panicnot42.warpbook.commands.CreateWaypointCommand;
import com.panicnot42.warpbook.commands.DeleteWaypointCommand;
import com.panicnot42.warpbook.commands.GiveWarpCommand;
import com.panicnot42.warpbook.commands.ListWaypointCommand;
import com.panicnot42.warpbook.crafting.WarpPageShapeless;
import com.panicnot42.warpbook.gui.GuiManager;
import com.panicnot42.warpbook.item.WarpBookItem;
import com.panicnot42.warpbook.item.WarpPageItem;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.net.packet.PacketWaypointName;

@Mod(modid = Properties.modid, name = Properties.name, version = Properties.version)
public class WarpBookMod
{
  @Instance(value = Properties.modid)
  public static WarpBookMod instance;

  public static final Logger logger = LogManager.getLogger("warpbook");
  public static final PacketPipeline packetPipeline = new PacketPipeline(logger, "warpbook");

  public static WarpBookItem warpBookItem;
  public static WarpPageItem warpPageItem;

  @SidedProxy(clientSide = "panicnot42.warpbook.client.ClientProxy", serverSide = "panicnot42.warpbook.Proxy")
  public static Proxy proxy;

  private static int guiIndex = 42;

  public static float exhaustionCoefficient;

  public static final int WarpBookWarpGuiIndex = guiIndex++;
  public static final int WarpBookWaypointGuiIndex = guiIndex++;
  public static final int WarpBookInventoryGuiIndex = guiIndex++;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    exhaustionCoefficient = (float)config.get("tweaks", "exhaustion coefficient", 20.0f).getDouble(20.0);
    warpBookItem = new WarpBookItem();
    warpPageItem = new WarpPageItem();
    GameRegistry.registerItem(warpBookItem, "warpbook");
    GameRegistry.registerItem(warpPageItem, "warppage");
    if (config.get("tweaks", "hard recipes", false).getBoolean(false))
    {
      GameRegistry.addShapelessRecipe(new ItemStack(warpBookItem), new ItemStack(Items.book), new ItemStack(Items.nether_star));
      GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new ItemStack(Items.paper), new ItemStack(Items.ender_eye));
    }
    else
    {
      GameRegistry.addShapelessRecipe(new ItemStack(warpBookItem), new ItemStack(Items.book), new ItemStack(Items.ender_pearl));
      GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new ItemStack(Items.paper), new ItemStack(Items.ender_pearl));
    }
    ItemStack boundpage = new ItemStack(warpPageItem);
    boundpage.setItemDamage(1);
    List<ItemStack> recipe = new ArrayList<ItemStack>();
    recipe.add(boundpage);
    recipe.add(new ItemStack(warpPageItem));
    GameRegistry.addRecipe(new WarpPageShapeless(boundpage, recipe));
    config.save();
  }

  @EventHandler
  public void init(FMLInitializationEvent event)
  {
    proxy.registerRenderers();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
    packetPipeline.initalise();
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    WarpWorldStorage.postInit();
    packetPipeline.registerPacket(PacketWarp.class);
    packetPipeline.registerPacket(PacketWaypointName.class);
    packetPipeline.postInitialise();
  }

  @EventHandler
  public void serverStarting(FMLServerStartingEvent event)
  {
    ServerCommandManager manager = ((ServerCommandManager)MinecraftServer.getServer().getCommandManager());
    manager.registerCommand(new CreateWaypointCommand());
    manager.registerCommand(new ListWaypointCommand());
    manager.registerCommand(new DeleteWaypointCommand());
    manager.registerCommand(new GiveWarpCommand());
  }
}
