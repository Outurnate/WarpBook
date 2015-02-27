package com.panicnot42.warpbook.client;

import com.panicnot42.warpbook.Proxy;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends Proxy
{
  @Override
  public void registerRenderers()
  {
  }
}
