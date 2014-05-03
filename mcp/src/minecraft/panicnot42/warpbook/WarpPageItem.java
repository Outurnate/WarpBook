package panicnot42.warpbook;

import java.math.RoundingMode;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WarpPageItem extends Item
{
	private static final String[] itemMetaNames = new String[] { "unbound",         "bound" };
	private static final String[] itemTextures  = new String[] { "unboundwarppage", "boundwarppage" };
	
	@SideOnly(Side.CLIENT)
	private Icon[] itemIcons;
	
	public WarpPageItem(int id)
	{
		super(id);
		super.setHasSubtypes(true).setMaxStackSize(1).setCreativeTab(CreativeTabs.tabTransport).setMaxDamage(0).setUnlocalizedName("warppageun");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamage(int meta)
	{
		return itemIcons[MathHelper.clamp_int(meta, 0, 1)];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return super.getUnlocalizedName() + "." + itemMetaNames[MathHelper.clamp_int(itemStack.getItemDamage(), 0, 1)];
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcons = new Icon[itemTextures.length];
		for (int i = 0; i < itemTextures.length; ++i)
			itemIcons[i] = iconRegister.registerIcon("warpbook:" + itemTextures[i]);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return 1;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (itemStack.getItemDamage() == 0)
		{
			itemStack.setItemDamage(1);
			if (!itemStack.hasTagCompound())
				itemStack.setTagCompound(new NBTTagCompound());
			itemStack.getTagCompound().setString("bindmsg", String.format("Bound to (%.0f, %.0f, %.0f) in dimension %d", player.posX, player.posY, player.posZ, player.dimension));
			itemStack.getTagCompound().setInteger("posX", MathUtils.round(player.posX, RoundingMode.DOWN));
			itemStack.getTagCompound().setInteger("posY", MathUtils.round(player.posY, RoundingMode.DOWN));
			itemStack.getTagCompound().setInteger("posZ", MathUtils.round(player.posZ, RoundingMode.DOWN));
			itemStack.getTagCompound().setInteger("dim",  player.dimension);
			player.openGui(WarpBook.instance, WarpBook.WarpBookWaypointGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		else if (player.isSneaking())
		{
			itemStack.setItemDamage(0);
			itemStack.setTagCompound(new NBTTagCompound());
		}
		return itemStack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean thing)
	{
		try
		{
			list.add(item.getTagCompound().getString("name"));
			list.add(item.getTagCompound().getString("bindmsg"));
		}
		catch(Exception e)
		{
			//gui hasn't closed
		}
	}
}