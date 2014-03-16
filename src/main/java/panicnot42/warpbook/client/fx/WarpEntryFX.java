package panicnot42.warpbook.client.fx;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import panicnot42.warpbook.WarpBookMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class WarpEntryFX extends EntityFX
{
  public WarpEntryFX(World world, double x, double y, double z, double vx, double vy, double vz)
  {
    super(world, x, y, z, vx, vy, vz);
    this.motionX = vx + (this.rand.nextFloat() - 0.5f) / 2.0f;
    this.motionY = vy + (this.rand.nextFloat() - 0.5f) / 2.0f;
    this.motionZ = vz + (this.rand.nextFloat() - 0.5f) / 2.0f;
    this.particleGreen = 0.0f;
    this.particleBlue = this.particleRed = this.rand.nextFloat() * 0.5f + 0.5f;
    this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
    this.particleMaxAge = (int)(16.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
  }
}
