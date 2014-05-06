package panicnot42.warpbook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class WarpBookWaypointContainer extends Container
{
  ItemStack heldItem;
  
  public WarpBookWaypointContainer(ItemStack heldItem)
  {
    this.heldItem = heldItem;
  }

  @Override
  public boolean canInteractWith(EntityPlayer entityplayer)
  {
    return true;
  }
}
