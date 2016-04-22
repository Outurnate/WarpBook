package com.panicnot42.warpbook.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Book - panicnot42
 * Created using Tabula 5.1.0
 */
public class Book extends ModelBase
{
  public ModelRenderer pages;
  public ModelRenderer cover;
  public ModelRenderer back;
  public ModelRenderer spine;

  public Book()
  {
    this.textureWidth = 64;
    this.textureHeight = 32;
    this.back = new ModelRenderer(this, 0, 9);
    this.back.setRotationPoint(-2.500000000000001F, 23.6F, -3.800000000000002F);
    this.back.addBox(0.0F, 0.0F, -0.35F, 5, 1, 8, 0.0F);
    this.pages = new ModelRenderer(this, 0, 18);
    this.pages.setRotationPoint(-2.500000000000001F, 22.6F, -3.800000000000002F);
    this.pages.addBox(0.0F, 0.0F, 0.0F, 5, 1, 8, 0.0F);
    this.cover = new ModelRenderer(this, 0, 0);
    this.cover.setRotationPoint(-2.500000000000001F, 22.4F, -3.800000000000002F);
    this.cover.addBox(0.0F, 0.0F, -0.35F, 5, 1, 8, 0.0F);
    this.spine = new ModelRenderer(this, 26, 9);
    this.spine.setRotationPoint(-2.700000000000001F, 22.4F, -4.200000000000001F);
    this.spine.addBox(0.0F, 0.0F, 0.0F, 1, 1, 8, 0.0F);
  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    GlStateManager.pushMatrix();
    GlStateManager.translate(this.back.offsetX, this.back.offsetY, this.back.offsetZ);
    GlStateManager.translate(this.back.rotationPointX * f5, this.back.rotationPointY * f5, this.back.rotationPointZ * f5);
    GlStateManager.scale(1.1D, 0.2D, 1.1D);
    GlStateManager.translate(-this.back.offsetX, -this.back.offsetY, -this.back.offsetZ);
    GlStateManager.translate(-this.back.rotationPointX * f5, -this.back.rotationPointY * f5, -this.back.rotationPointZ * f5);
    this.back.render(f5);
    GlStateManager.popMatrix();
    this.pages.render(f5);
    GlStateManager.pushMatrix();
    GlStateManager.translate(this.cover.offsetX, this.cover.offsetY, this.cover.offsetZ);
    GlStateManager.translate(this.cover.rotationPointX * f5, this.cover.rotationPointY * f5, this.cover.rotationPointZ * f5);
    GlStateManager.scale(1.1D, 0.2D, 1.1D);
    GlStateManager.translate(-this.cover.offsetX, -this.cover.offsetY, -this.cover.offsetZ);
    GlStateManager.translate(-this.cover.rotationPointX * f5, -this.cover.rotationPointY * f5, -this.cover.rotationPointZ * f5);
    this.cover.render(f5);
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(this.spine.offsetX, this.spine.offsetY, this.spine.offsetZ);
    GlStateManager.translate(this.spine.rotationPointX * f5, this.spine.rotationPointY * f5, this.spine.rotationPointZ * f5);
    GlStateManager.scale(0.2D, 1.4D, 1.1D);
    GlStateManager.translate(-this.spine.offsetX, -this.spine.offsetY, -this.spine.offsetZ);
    GlStateManager.translate(-this.spine.rotationPointX * f5, -this.spine.rotationPointY * f5, -this.spine.rotationPointZ * f5);
    this.spine.render(f5);
    GlStateManager.popMatrix();
  }

  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
  {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }
}
