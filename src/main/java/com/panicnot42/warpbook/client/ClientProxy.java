package com.panicnot42.warpbook.client;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.Proxy;
import com.panicnot42.warpbook.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelBakery;

@SideOnly(Side.CLIENT)
public class ClientProxy extends Proxy
{
  @Override
  public void registerModels()
  {
/*    ModelBakery.addVariantName(WarpBookMod.warpPageItem,
                               Properties.modid + ":warppage",
                               Properties.modid + ":warppage_bound",
                               Properties.modid + ":warppage_hyperbound",
                               Properties.modid + ":warppage_deathly",
                               Properties.modid + ":warppage_potato",
                               Properties.modid + ":warppage_player");*/
  }
  
  @Override
  public void registerRenderers()
  {
    RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

    renderItem.getItemModelMesher()
      .register(WarpBookMod.unboundWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":unboundwarppage", "inventory"));
    renderItem.getItemModelMesher()
      .register(WarpBookMod.boundWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":boundwarppage", "inventory"));
    renderItem.getItemModelMesher()
      .register(WarpBookMod.hyperWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":hyperwarppage", "inventory"));
    renderItem.getItemModelMesher()
        .register(WarpBookMod.deathlyWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":deathlywarppage", "inventory"));
    renderItem.getItemModelMesher()
        .register(WarpBookMod.potatoWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":potatowarppage", "inventory"));
    renderItem.getItemModelMesher()
      .register(WarpBookMod.playerWarpPageItem, 0, new ModelResourceLocation(Properties.modid + ":playerwarppage", "inventory"));
  }

  @Override
  public void postInit()
  {
    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
      .register(WarpBookMod.warpBookItem, 0, new ModelResourceLocation(Properties.modid.toLowerCase() + ":warpbook", "inventory"));
//    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//    .register(WarpBookMod.warpPageItem, 0, new ModelResourceLocation(Properties.modid.toLowerCase() + ":warppage", "inventory"));
  }
}
