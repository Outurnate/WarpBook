package com.panicnot42.warpbook.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen
{
  private final EntityPlayer entityPlayer;
  private NBTTagList items;
  private static final ResourceLocation invBg = new ResourceLocation("warpbook", "textures/gui/book.png");
  private int xSize, ySize, page, pageCount;
  private GuiButton next, prev;
  private ArrayList<GuiButton> warps;
  private ArrayList<ButtonPos> pos;

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
    xSize = 146;
    ySize = 180;
    page = 0;
    if (!entityPlayer.getHeldItemMainhand().hasTagCompound()) entityPlayer.getHeldItemMainhand().setTagCompound(new NBTTagCompound());
    items = entityPlayer.getHeldItemMainhand().getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
    if (items.tagCount() == 0)
    {
      CommandUtils.showError(entityPlayer, I18n.format("help.nopages"));
      mc.displayGuiScreen((GuiScreen)null);
      return;
    }
    pos = new ArrayList<ButtonPos>();
    for (int i = 0; i < items.tagCount(); ++i)
    {
      ItemStack stack = new ItemStack(items.getCompoundTagAt(i));
      if (stack.getItem() instanceof IDeclareWarp && ((IDeclareWarp)stack.getItem()).ValidData(stack))
        pos.add(new ButtonPos(i, ((IDeclareWarp)stack.getItem()).GetName(entityPlayer.getEntityWorld(), stack)));
    }
    int x = width / 2 - 48;
    int y = 20 + (height / 2) - (ySize / 2);
    int n = Math.min(9, pos.size());
    warps = new ArrayList<GuiButton>();
    for (int i = 0; i < n; ++i)
    {
      GuiButton but = new GuiButton(n, x, y + (16 * i), 96, 12, "");
      buttonList.add(but);
      warps.add(but);
    }
    y = height / 2 + ySize / 2 + 8;
    pageCount = pos.size() / 9;
    if (pageCount != 0)
    {
      buttonList.add(prev = new GuiButton(64, x, y, 32, 12, "<<<"));
      buttonList.add(next = new GuiButton(65, x + 64, y, 32, 12, ">>>"));
    }
    updateButtonStat();
  }

  private void updateButtonStat()
  {
    if (prev != null)
      prev.enabled = page != 0;
    if (next != null)
      next.enabled = pageCount - 1 != page;
    int r = pos.size() - page * 9;
    int n = Math.min(r, 9);
    for (int i = 0; i < n; ++i)
    {
      int j = page * 9 + i;
      warps.get(i).id = pos.get(j).id;
      warps.get(i).displayString = pos.get(j).name;
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
    switch (guiButton.id)
    {
    case 64:
      --page;
      updateButtonStat();
      break;
      
    case 65:
      ++page;
      updateButtonStat();
      break;
      
    default:
      PacketWarp packet = new PacketWarp(guiButton.id);
      ItemStack page = PacketWarp.getPageById(entityPlayer, guiButton.id);
      WarpBookMod.warpDrive.handleWarp(Minecraft.getMinecraft().player, page);
      WarpBookMod.network.sendToServer(packet);
      mc.displayGuiScreen((GuiScreen)null);
      break;
    }
  }

  @Override
  public void drawScreen(int par1, int par2, float par3)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.getTextureManager().bindTexture(invBg);
    drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 20, 1, xSize, ySize);
    drawCenteredString(fontRenderer, I18n.format("warpbook.dowarp"), width / 2, (height / 2) - ySize / 2 - 12, 0xFFFFFF);
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
      mc.player.closeScreen();
    }
  }
}
