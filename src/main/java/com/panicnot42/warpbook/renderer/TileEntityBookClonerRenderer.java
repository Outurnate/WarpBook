package com.panicnot42.warpbook.renderer;

import org.lwjgl.opengl.GL11;

import com.panicnot42.warpbook.Properties;
import com.panicnot42.warpbook.models.BookCloner;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class TileEntityBookClonerRenderer extends TileEntitySpecialRenderer
{
  private final BookCloner model;
  private final ResourceLocation texture;

  public TileEntityBookClonerRenderer()
  {
    this.model = new BookCloner();
    this.texture = new ResourceLocation(Properties.modid + ":textures/blocks/bookcloner.png");
  }

  @Override
  public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale, int d)
  {
    GL11.glPushMatrix();
    GL11.glTranslatef((float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f);
    GL11.glPushMatrix();
    GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
    Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    this.model.render(null, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
    GL11.glPopMatrix();
    GL11.glPopMatrix();
  }

  private void adjustLightFixture(World world, int i, int j, int k, Block block)
  {
    BlockPos pos = new BlockPos(i, j, k);
    Tessellator tes = Tessellator.getInstance();
    float brightness = block.getLightValue(world, pos);
    int skylight = world.getLightFor(EnumSkyBlock.SKY, pos);
    int modulusModifier = skylight % 65536;
    int divModifier = skylight / 65536;
    //tes.setColorOpaque(brightness, brightness, brightness);
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)modulusModifier, divModifier);
  }
}
