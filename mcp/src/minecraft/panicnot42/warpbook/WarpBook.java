package panicnot42.warpbook;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "warpbook", name = "Warp Book", version = "0.0.200b")
@NetworkMod(clientSideRequired = true, channels = { "WarpBookWaypoint", "WarpBookWarp", "WarpBookParticle" }, packetHandler = PacketHandler.class)
public class WarpBook
{
	@Instance(value = "warpbook")
	public static WarpBook instance;
	
	private static Item warpBookItem;
	private static Item warpPageItem;

	@SidedProxy(clientSide="panicnot42.warpbook.client.ClientProxy", serverSide="panicnot42.warpbook.CommonProxy")
	public static CommonProxy proxy;
	
	private static int guiIndex = 42;

	public static final int WarpBookWarpGuiIndex      = guiIndex++;
	public static final int WarpBookWaypointGuiIndex  = guiIndex++;
	public static final int WarpBookInventoryGuiIndex = guiIndex++;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		warpBookItem = new WarpBookItem(config.getItem("warpBook", 4200).getInt());
		warpPageItem = new WarpPageItem(config.getItem("warpPage", 4201).getInt());
		config.save();
		GameRegistry.registerItem(warpBookItem, "warpbookitem");
		GameRegistry.registerItem(warpPageItem, "warppageitem");
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		GameRegistry.addShapelessRecipe(new ItemStack(warpBookItem), new ItemStack(Item.book),  new ItemStack(Item.enderPearl));
		GameRegistry.addShapelessRecipe(new ItemStack(warpPageItem), new ItemStack(Item.paper), new ItemStack(Item.enderPearl));
		LanguageRegistry.addName(warpBookItem, "Warp Book");
		LanguageRegistry.instance().addStringLocalization("item.warppageun.unbound.name", "Unbound Warp Page");
		LanguageRegistry.instance().addStringLocalization("item.warppageun.bound.name",     "Bound Warp Page");
		LanguageRegistry.instance().addStringLocalization("warpbook.dowarp",       "Warp to Waypoint");
		LanguageRegistry.instance().addStringLocalization("warpbook.bindpage",     "Bind Page");
		LanguageRegistry.instance().addStringLocalization("warpbook.namewaypoint", "Enter waypoint name");
		proxy.registerRenderers();
		NetworkRegistry.instance().registerGuiHandler(this, new GuiManager());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
