package panicnot42.warpbook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class WarpBookInventorySlot extends Slot
{
	public WarpBookInventorySlot(InventoryPlayer inventory, int i, int j, int k)
	{
		super(inventory, i, j, k);
	}
	
	@Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
		return getHasStack() && WarpBookSlot.itemValid(getStack());
    }
}
