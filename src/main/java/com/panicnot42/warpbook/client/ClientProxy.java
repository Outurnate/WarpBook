package com.panicnot42.warpbook.client;

import com.panicnot42.warpbook.Properties;
import com.panicnot42.warpbook.Proxy;
import com.panicnot42.warpbook.WarpBookMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends Proxy
{
  @Override
  public void registerRenderers()
  {
    ItemModelMesher m = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

    m.register(WarpBookMod.items.unboundWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":unboundwarppage", "inventory"));
    m.register(WarpBookMod.items.boundWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":boundwarppage", "inventory"));
    m.register(WarpBookMod.items.hyperWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":hyperwarppage", "inventory"));
    m.register(WarpBookMod.items.deathlyWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":deathlywarppage", "inventory"));
    m.register(WarpBookMod.items.potatoWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":potatowarppage", "inventory"));
    m.register(WarpBookMod.items.playerWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":playerwarppage", "inventory"));
    m.register(WarpBookMod.items.warpBookItem, 0, new ModelResourceLocation(Properties.modid + ":warpbook", "inventory"));
    m.register(WarpBookMod.items.warpPrintingPlateItem, 0, new ModelResourceLocation(Properties.modid + ":warpplate", "inventory"));
    if (WarpBookMod.fuelEnabled)
      m.register(WarpBookMod.items.warpFuelItem, 0, new ModelResourceLocation(Properties.modid + ":warpfuel", "inventory"));

    m.register(Item.getItemFromBlock(WarpBookMod.blocks.bookCloner), 0, new ModelResourceLocation(Properties.modid + ":bookcloner", "inventory"));
    m.register(Item.getItemFromBlock(WarpBookMod.blocks.teleporter), 0, new ModelResourceLocation(Properties.modid + ":teleporter", "inventory"));
  }
}
