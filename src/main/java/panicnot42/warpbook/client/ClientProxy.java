package panicnot42.warpbook.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import panicnot42.warpbook.Proxy;
import panicnot42.warpbook.util.Waypoint;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
    Waypoint wp = extractWaypoint(player, page);
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
    {
      int particles = (2 - Minecraft.getMinecraft().gameSettings.particleSetting) * 50;
      if (player.dimension == wp.dim)
        for (int i = 0; i < particles; ++i)
          player.worldObj.spawnParticle("portal", wp.x + 0.5D, wp.y + (player.worldObj.rand.nextDouble() * 2), wp.z + 0.5D, player.worldObj.rand.nextDouble() - 0.5D,
              player.worldObj.rand.nextDouble() - 0.5D, player.worldObj.rand.nextDouble() - 0.5D);
      for (int i = 0; i < (5 * particles); ++i)
        player.worldObj.spawnParticle("largesmoke", player.posX, player.posY + (player.worldObj.rand.nextDouble() * 2), player.posZ, (player.worldObj.rand.nextDouble() / 10) - 0.05D, 0D,
            (player.worldObj.rand.nextDouble() / 10) - 0.05D);
    }
    super.handleWarp(player, page);
  }
}
