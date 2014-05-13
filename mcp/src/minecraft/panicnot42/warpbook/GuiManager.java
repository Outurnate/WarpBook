package panicnot42.warpbook;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
    System.out.println(FMLCommonHandler.instance().getEffectiveSide().toString() + player.getHeldItem());
		if (ID == WarpBook.WarpBookInventoryGuiIndex)
			return new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(player.getHeldItem()));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
    System.out.println(FMLCommonHandler.instance().getEffectiveSide().toString() + player.getHeldItem());
    //Thread.dumpStack();
		if (ID == WarpBook.WarpBookWarpGuiIndex)
			return new GuiBook(player);
		if (ID == WarpBook.WarpBookInventoryGuiIndex)
			return new GuiWarpBookItemInventory(new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(player.getHeldItem())));
		if (ID == WarpBook.WarpBookWaypointGuiIndex)
			return new GuiWaypointName(player);
		return null;
	}
}
