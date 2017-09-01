package com.panicnot42.warpbook.core;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class WarpBookTeleporter extends Teleporter
{
  private final WorldServer server;
  private double x;
  private double y;
  private double z;
  
  public WarpBookTeleporter(WorldServer server, double x, double y, double z)
  {
    super(server);
    this.server = server;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  @Override
  public void placeInPortal(Entity entity, float rotationYaw)
  {
    this.server.getBlockState(new BlockPos((int)x, (int)y, (int)z));
    entity.setPosition(x, y, z);
    entity.motionZ = entity.motionY = entity.motionX = 0.0f;
  }
}
