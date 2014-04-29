package panicnot42.warpbook;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WarpBookItem extends Item
{
	public WarpBookItem(int id)
	{
		super(id);
		this.setMaxStackSize(1).setCreativeTab(CreativeTabs.tabTransport).setUnlocalizedName("warpbook").setTextureName("warpbook:warpbook");
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return 1;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if(player.isSneaking())
			player.openGui(WarpBook.instance, WarpBook.WarpBookInventoryGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		else
			player.openGui(WarpBook.instance, WarpBook.WarpBookWarpGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return itemStack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean thing)
	{
		try
		{
			list.add(String.format("%d/54 pages", item.getTagCompound().getTagList("WarpPages").tagCount()));
		}
		catch(Exception e)
		{
			// no pages
		}
	}
}
