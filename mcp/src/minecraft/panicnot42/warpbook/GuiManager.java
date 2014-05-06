package panicnot42.warpbook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == WarpBook.WarpBookInventoryGuiIndex)
			return new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(player.getHeldItem()));
    if (ID == WarpBook.WarpBookWarpGuiIndex)
      return new WarpBookWaypointContainer(player.getHeldItem());
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == WarpBook.WarpBookWarpGuiIndex)
			return (player.getHeldItem().getItem() instanceof WarpBookItem) ? new GuiBook(player) : null;
		if (ID == WarpBook.WarpBookInventoryGuiIndex)
			return new GuiWarpBookItemInventory(new WarpBookContainerItem(player, player.inventory, new WarpBookInventoryItem(player.getHeldItem())));
		if (ID == WarpBook.WarpBookWaypointGuiIndex)
			return new GuiWaypointName(player);
		return null;
	}
}
