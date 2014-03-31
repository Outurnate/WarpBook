package panicnot42.warpbook.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.lwjgl.input.Keyboard;

import panicnot42.util.CommandUtils;
import panicnot42.util.StringUtils;
import panicnot42.warpbook.WarpBookMod;
import panicnot42.warpbook.item.WarpPageItem;
import panicnot42.warpbook.net.packet.PacketWarp;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen
{
  private final EntityPlayer entityPlayer;
  private NBTTagList items;

  public GuiBook(EntityPlayer entityPlayer)
  {
    this.entityPlayer = entityPlayer;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void initGui()
  {
    Keyboard.enableRepeatEvents(true);
    buttonList.clear();
    if (!entityPlayer.getHeldItem().hasTagCompound()) entityPlayer.getHeldItem().setTagCompound(new NBTTagCompound());
    items = entityPlayer.getHeldItem().getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    if (items.tagCount() == 0)
    {
      CommandUtils.showError(entityPlayer, "There are no pages in this book.  Shift+right click to add bound pages");
      mc.displayGuiScreen((GuiScreen)null);
      return;
    }
    for (int i = 0; i < items.tagCount(); ++i)
    {
      NBTTagCompound compound = ItemStack.loadItemStackFromNBT((NBTTagCompound)items.getCompoundTagAt(i)).getTagCompound();
      try
      {
        buttonList.add(new GuiButton(i, ((width - 404) / 2) + ((i % 6) * 68), 16 + (24 * (i / 6)), 64, 16, StringUtils.shorten(compound.hasKey("hypername") ? compound.getString("hypername")
            : compound.getString("name"), 10)));
      }
      catch (Exception e)
      {
        // old page
      }
    }
  }

  @Override
  public void onGuiClosed()
  {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void actionPerformed(GuiButton guiButton)
  {
    PacketWarp packet = new PacketWarp(guiButton.id);
    ItemStack page = PacketWarp.getPageById(entityPlayer, guiButton.id);
    WarpBookMod.proxy.handleWarp(Minecraft.getMinecraft().thePlayer, page);
    WarpBookMod.packetPipeline.sendToServer(packet);

    mc.displayGuiScreen((GuiScreen)null);
  }

  @Override
  public void drawScreen(int par1, int par2, float par3)
  {
    drawDefaultBackground();
    drawCenteredString(fontRendererObj, I18n.format("warpbook.dowarp"), width / 2, 6, 16777215);
    super.drawScreen(par1, par2, par3);
  }
}
