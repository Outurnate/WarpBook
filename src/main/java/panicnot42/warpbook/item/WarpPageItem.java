package panicnot42.warpbook.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import panicnot42.util.CommandUtils;
import panicnot42.warpbook.WarpBookMod;
import panicnot42.warpbook.WarpWorldStorage;
import panicnot42.warpbook.client.fx.WarpEntryFX;
import panicnot42.warpbook.util.Waypoint;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WarpPageItem extends Item
{
  private static final String[] itemMetaNames = new String[] { "unbound", "bound", "hyperbound" };
  private static final String[] itemTextures = new String[] { "unboundwarppage", "boundwarppage", "hyperboundwarppage" };

  @SideOnly(Side.CLIENT)
  private IIcon[] itemIcons;
  
  @SideOnly(Side.CLIENT)
  public IIcon warpExitParticleFX; // TODO this feels wrong

  public WarpPageItem()
  {
    super.setHasSubtypes(true).setMaxStackSize(1).setCreativeTab(CreativeTabs.tabTransport).setMaxDamage(0).setUnlocalizedName("warppage");
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IIcon getIconFromDamage(int meta)
  {
    return itemIcons[MathHelper.clamp_int(meta, 0, 2)];
  }

  @Override
  public String getUnlocalizedName(ItemStack itemStack)
  {
    return super.getUnlocalizedName() + "." + itemMetaNames[MathHelper.clamp_int(itemStack.getItemDamage(), 0, 2)];
  }

  @Override
  public void registerIcons(IIconRegister iconRegister)
  {
    itemIcons = new IIcon[itemTextures.length];
    for (int i = 0; i < itemTextures.length; ++i)
      itemIcons[i] = iconRegister.registerIcon("warpbook:" + itemTextures[i]);
    warpExitParticleFX = iconRegister.registerIcon("warpbook:warpparticle");
  }

  @Override
  public int getMaxItemUseDuration(ItemStack itemStack)
  {
    return 1;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
  {
    if (player.isSneaking())
    {
      switch (itemStack.getItemDamage())
      {
        case 0:
          break;
        case 1:
          itemStack.setItemDamage(0);
          itemStack.setTagCompound(new NBTTagCompound());
          break;
      }
    }
    else
    {
      switch (itemStack.getItemDamage())
      {
        case 0:
          itemStack.setItemDamage(1);
          if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
          itemStack.getTagCompound().setString("bindmsg", String.format("Bound to (%.0f, %.0f, %.0f) in dimension %d", player.posX, player.posY, player.posZ, player.dimension));
          itemStack.getTagCompound().setInteger("posX", MathHelper.ceiling_double_int(player.posX));
          itemStack.getTagCompound().setInteger("posY", MathHelper.ceiling_double_int(player.posY));
          itemStack.getTagCompound().setInteger("posZ", MathHelper.ceiling_double_int(player.posZ));
          itemStack.getTagCompound().setInteger("dim", player.dimension);
          player.openGui(WarpBookMod.instance, WarpBookMod.WarpBookWaypointGuiIndex, world, (int)player.posX, (int)player.posY, (int)player.posZ);
          break;
        case 1:
        case 2:
          WarpBookMod.proxy.handleWarp(player, itemStack);
          if (!player.capabilities.isCreativeMode) --itemStack.stackSize;
          break;
      }
    }
    return itemStack;
  }

  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack item, EntityPlayer player, @SuppressWarnings("rawtypes") List list, boolean thing)
  {
    switch (item.getItemDamage())
    {
      case 1:
        try
        {
          list.add(item.getTagCompound().getString("name"));
          list.add(item.getTagCompound().getString("bindmsg"));
        }
        catch (Exception e)
        {
          // gui hasn't closed
        }
        break;
      case 2:
        String name = item.getTagCompound().getString("hypername");
        list.add(name);
        break;
    }
  }

  @Override
  public boolean hasContainerItem(ItemStack itemStack)
  {
    return itemStack.getItemDamage() == 1;
  }

  @Override
  public ItemStack getContainerItem(ItemStack itemStack)
  {
    return itemStack.getItemDamage() == 1 ? itemStack.copy() : null;
  }
}
