package com.panicnot42.warpbook;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class WarpBookTeleporter extends Teleporter
{
  public WarpBookTeleporter(WorldServer par1WorldServer)
  {
    super(par1WorldServer);
  }

  @Override
  public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
  {
  }

  @Override
  public void removeStalePortalLocations(long par1)
  {
  }
}
