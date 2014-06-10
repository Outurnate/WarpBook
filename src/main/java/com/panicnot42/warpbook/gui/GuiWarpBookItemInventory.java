package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.inventory.WarpBookInventoryItem;
import com.panicnot42.warpbook.inventory.container.WarpBookContainerItem;

public class GuiWarpBookItemInventory extends GuiContainer
{
  private static final ResourceLocation iconLocation = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

  private final WarpBookInventoryItem inventory;

  public GuiWarpBookItemInventory(WarpBookContainerItem warpBookContainerItem)
  {
    super(warpBookContainerItem);
    inventory = warpBookContainerItem.inventory;
    xSize = 176;
    ySize = 222;
  }

  @Override
  public void drawScreen(int xsize, int ysize, float par3)
  {
    super.drawScreen(xsize, ysize, par3);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    String s = inventory.getInventoryName();
    fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.getTextureManager().bindTexture(iconLocation);
    int k = (width - xSize) / 2;
    int l = (height - ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
  }
}
