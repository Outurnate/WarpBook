package com.panicnot42.warpbook;

import com.panicnot42.warpbook.block.BlockBookCloner;
import com.panicnot42.warpbook.block.BlockTeleporter;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class WarpBlocks {
	public BlockBookCloner bookCloner;
	public BlockTeleporter teleporter;
	
	public WarpBlocks() {
		bookCloner = new BlockBookCloner();
		teleporter = new BlockTeleporter();
	}
	
	public void register(IForgeRegistry<Block> registry) {
		GameRegistry.registerTileEntity(TileEntityBookCloner.class, "tileEntityBookCloner");
		GameRegistry.registerTileEntity(TileEntityTeleporter.class, "tileEntityTeleporter");
		
		registry.register(bookCloner);
		registry.register(teleporter);
	}
	
}
