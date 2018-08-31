package com.panicnot42.warpbook;

import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class WarpBookTeleporter extends Teleporter {
	public WarpBookTeleporter(WorldServer par1WorldServer) {
		super(par1WorldServer);
	}
	
	@Override
	public void removeStalePortalLocations(long par1) {
	}
}
