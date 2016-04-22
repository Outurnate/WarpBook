package com.panicnot42.warpbook.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Book Cloner - panicnot42
 * Created using Tabula 5.1.0
 */
public class BookCloner extends ModelBase
{
  public ModelRenderer base;
  public ModelRenderer leftLip;
  public ModelRenderer rightLip;
  public ModelRenderer frontLip;
  public ModelRenderer backLip;
  public ModelRenderer bookBase;
  
  public BookCloner()
  {
    this.textureWidth = 64;
    this.textureHeight = 64;
    this.bookBase = new ModelRenderer(this, 18, 26);
    this.bookBase.setRotationPoint(-4.5F, 15.0F, -4.5F);
    this.bookBase.addBox(0.0F, 0.0F, 0.0F, 9, 1, 9, 0.0F);
    this.base = new ModelRenderer(this, 0, 0);
    this.base.setRotationPoint(-8.0F, 16.0F, -8.0F);
    this.base.addBox(0.0F, 0.0F, 0.0F, 16, 8, 16, 0.0F);
    this.rightLip = new ModelRenderer(this, 0, 24);
    this.rightLip.setRotationPoint(7.0F, 15.0F, -8.0F);
    this.rightLip.addBox(0.0F, 0.0F, 0.0F, 1, 1, 16, 0.0F);
    this.backLip = new ModelRenderer(this, 18, 24);
    this.backLip.setRotationPoint(-7.0F, 15.0F, 7.0F);
    this.backLip.addBox(0.0F, 0.0F, 0.0F, 14, 1, 1, 0.0F);
    this.leftLip = new ModelRenderer(this, 0, 24);
    this.leftLip.setRotationPoint(-8.0F, 15.0F, -8.0F);
    this.leftLip.addBox(0.0F, 0.0F, 0.0F, 1, 1, 16, 0.0F);
    this.frontLip = new ModelRenderer(this, 18, 24);
    this.frontLip.setRotationPoint(-7.0F, 15.0F, -8.0F);
    this.frontLip.addBox(0.0F, 0.0F, 0.0F, 14, 1, 1, 0.0F);
  }

  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    this.bookBase.render(f5);
    this.base.render(f5);
    this.rightLip.render(f5);
    this.backLip.render(f5);
    this.leftLip.render(f5);
    this.frontLip.render(f5);
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
