package panicnot42.warpbook;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import panicnot42.util.PacketPipeline;
import panicnot42.warpbook.commands.CreateWaypointCommand;
import panicnot42.warpbook.commands.DeleteWaypointCommand;
import panicnot42.warpbook.commands.GiveWarpCommand;
import panicnot42.warpbook.commands.ListWaypointCommand;
import panicnot42.warpbook.crafting.WarpPageShapeless;
import panicnot42.warpbook.gui.GuiManager;
import panicnot42.warpbook.item.WarpBookItem;
import panicnot42.warpbook.item.WarpPageItem;
import panicnot42.warpbook.net.packet.PacketWarp;
import panicnot42.warpbook.net.packet.PacketWaypointName;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = WarpBookMod.modid, name = "Warp Book", version = "0.1.332")
public class WarpBookMod
{
  @Instance(value = "warpbook")
  public static WarpBookMod instance;

  public static final Logger logger = LogManager.getLogger("warpbook");
  public static final PacketPipeline packetPipeline = new PacketPipeline(logger, "warpbook");

  public static Item warpBookItem;
  public static Item warpPageItem;

  @SidedProxy(clientSide = "panicnot42.warpbook.client.ClientProxy", serverSide = "panicnot42.warpbook.CommonProxy")
  public static CommonProxy proxy;

  private static int guiIndex = 42;

  public static float exhaustionCoefficient;

  public static final int WarpBookWarpGuiIndex = guiIndex++;
  public static final int WarpBookWaypointGuiIndex = guiIndex++;
  public static final int WarpBookInventoryGuiIndex = guiIndex++;

  public static final String modid = "warpbook";

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
  public void load(FMLInitializationEvent event)
  {
    proxy.registerRenderers();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiManager());
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
