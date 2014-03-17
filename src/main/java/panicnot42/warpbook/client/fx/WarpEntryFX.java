package panicnot42.warpbook.client.fx;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import panicnot42.warpbook.WarpBookMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class WarpEntryFX extends EntityFX
{
  public WarpEntryFX(World world, double x, double y, double z, double vx, double vy, double vz, IIcon tex)
  {
    super(world, x, y, z, vx, vy, vz);
    this.motionX = vx + (this.rand.nextFloat() - 0.5f) / 2.0f;
    this.motionY = vy + (this.rand.nextFloat() - 0.5f) / 2.0f;
    this.motionZ = vz + (this.rand.nextFloat() - 0.5f) / 2.0f;
    this.particleGreen = 0.0f;
    this.particleBlue = this.particleRed = this.rand.nextFloat() * 0.5f + 0.5f;
    this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
    this.particleMaxAge = (int)(16.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
    this.particleIcon = tex;
  }
  
  /*@Override
  public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
  {
      float f6 = (float)this.particleTextureIndexX / 16.0F;
      float f7 = f6 + 0.0624375F;
      float f8 = (float)this.particleTextureIndexY / 16.0F;
      float f9 = f8 + 0.0624375F;
      float f10 = 0.1F * this.particleScale;

      if (this.particleIcon != null)
      {
          f6 = this.particleIcon.getMinU();
          f7 = this.particleIcon.getMaxU();
          f8 = this.particleIcon.getMinV();
          f9 = this.particleIcon.getMaxV();
      }

      float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
      float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
      float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
      par1Tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
      par1Tessellator.addVertexWithUV((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10), (double)f7, (double)f9);
      par1Tessellator.addVertexWithUV((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10), (double)f7, (double)f8);
      par1Tessellator.addVertexWithUV((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10), (double)f6, (double)f8);
      par1Tessellator.addVertexWithUV((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10), (double)f6, (double)f9);
  }*/
}
