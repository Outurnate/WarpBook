package panicnot42.warpbook.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import panicnot42.warpbook.Proxy;
import panicnot42.warpbook.WarpBookMod;
import panicnot42.warpbook.client.fx.WarpEntryFX;

@SideOnly(Side.CLIENT)
public class ClientProxy extends Proxy
{
  @Override
  public void registerRenderers()
  {
  }
  
  @Override
  public void handleWarp(EntityPlayer player, ItemStack page)
  {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
      for (int i = 0; i < 1000; ++i)
      {
        EntityFX fx = new WarpEntryFX(player.worldObj, 0, 4 + (player.worldObj.rand.nextFloat() * 2.0f), 0, 0, 0, 0, WarpBookMod.warpPageItem.warpExitParticleFX);
        //fx.setParticleIcon(WarpBookMod.warpPageItem.warpExitParticleFX);
        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
      }
    super.handleWarp(player, page);
  }
}
