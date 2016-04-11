package com.panicnot42.warpbook.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.util.CommandUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen
{
  private final EntityPlayer entityPlayer;
  private NBTTagList items;

  private class ButtonPos
  {
    public int id;
    public String name;

    public ButtonPos(int id, String name)
    {
      this.id = id;
      this.name = name;
    }
  };

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
    ArrayList<ButtonPos> pos = new ArrayList<ButtonPos>();
    for (int i = 0; i < items.tagCount(); ++i)
    {
      ItemStack stack = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(i));
      if (stack.getItem() instanceof IDeclareWarp && ((IDeclareWarp)stack.getItem()).ValidData(stack))
        pos.add(new ButtonPos(i, ((IDeclareWarp)stack.getItem()).GetName(entityPlayer.getEntityWorld(), stack)));
    }
    for (int i = 0; i < pos.size(); ++i)
      buttonList.add(new GuiButton(pos.get(i).id, ((width - 404) / 2) + ((i % 6) * 68), 16 + (24 * (i / 6)), 64, 16, pos.get(i).name));
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
    WarpBookMod.warpDrive.handleWarp(Minecraft.getMinecraft().thePlayer, page);
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

  @Override
  protected void keyTyped(char c, int keyCode) throws IOException
  {
    super.keyTyped(c, keyCode);
    if (c == 1 || c == 'e') // if the player rebinds, this won't work. I don't care
    {
      mc.thePlayer.closeScreen();
    }
  }
}
