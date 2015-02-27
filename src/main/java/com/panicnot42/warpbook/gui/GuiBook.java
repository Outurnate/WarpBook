package com.panicnot42.warpbook.gui;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.lwjgl.input.Keyboard;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.util.CommandUtils;
import com.panicnot42.warpbook.util.PlayerUtils;
import com.panicnot42.warpbook.util.StringUtils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
      CommandUtils.showError(entityPlayer, I18n.format("help.nopages"));
      mc.displayGuiScreen((GuiScreen)null);
      return;
    }
    for (int i = 0; i < items.tagCount(); ++i)
    {
      NBTTagCompound compound = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i)).getTagCompound();
      try
      {
        buttonList.add(new GuiButton(i, ((width - 404) / 2) + ((i % 6) * 68), 16 + (24 * (i / 6)), 64, 16, getButtonText(compound)));
      }
      catch (Exception e)
      {
        // old page
      }
    }
  }

  private static String getButtonText(NBTTagCompound compound)
  {
    if (compound.hasKey("hypername"))
      return StringUtils.shorten(compound.getString("hypername"), 10);
    else if (compound.hasKey("name"))
      return StringUtils.shorten(compound.getString("name"), 10);
    else if (compound.hasKey("playeruuid"))
      return StringUtils.shorten(PlayerUtils.getNameByUUID(UUID.fromString(compound.getString("playeruuid"))), 10);
    else
      return "";
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
    WarpBookMod.network.sendToServer(packet);

    mc.displayGuiScreen((GuiScreen)null);
  }

  @Override
  public void drawScreen(int par1, int par2, float par3)
  {
    drawDefaultBackground();
    drawCenteredString(fontRendererObj, I18n.format("warpbook.dowarp"), width / 2, 6, 16777215);
    super.drawScreen(par1, par2, par3);
  }

  @Override
  public boolean doesGuiPauseGame()
  {
    return false;
  }
}
