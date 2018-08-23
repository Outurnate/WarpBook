package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.InventoryBookCloner;
import com.panicnot42.warpbook.inventory.InventoryWarpBook;
import com.panicnot42.warpbook.inventory.container.ContainerBookCloner;
import com.panicnot42.warpbook.inventory.container.ContainerWarpBook;
import com.panicnot42.warpbook.inventory.container.InventoryWarpBookSpecial;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == WarpBookMod.WarpBookInventoryGuiIndex) {
			return new ContainerWarpBook(player, player.inventory,
					new InventoryWarpBook(player.getHeldItemMainhand()), new InventoryWarpBookSpecial(player.getHeldItemMainhand()));
		}
		if (ID == WarpBookMod.BookClonerInventoryGuiIndex) {
			return new ContainerBookCloner(player, player.inventory,
					new InventoryBookCloner((TileEntityBookCloner)world.getTileEntity(new BlockPos(x, y, z))));
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == WarpBookMod.WarpBookWarpGuiIndex) {
			return new GuiBook(player);
		}
		if (ID == WarpBookMod.WarpBookInventoryGuiIndex) {
			return new GuiWarpBookItemInventory(new ContainerWarpBook(player, player.inventory,
					new InventoryWarpBook(player.getHeldItemMainhand()),
					new InventoryWarpBookSpecial(player.getHeldItemMainhand())));
		}
		if (ID == WarpBookMod.WarpBookWaypointGuiIndex) {
			return new GuiWaypointName(player);
		}
		if (ID == WarpBookMod.BookClonerInventoryGuiIndex) {
			return new GuiBookCloner(new ContainerBookCloner(player, player.inventory,
					new InventoryBookCloner((TileEntityBookCloner)
							world.getTileEntity(new BlockPos(x, y, z)))));
		}
		return null;
	}
}
