package com.panicnot42.warpbook.gui;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.net.packet.PacketWaypointName;
import com.sun.org.apache.xml.internal.security.utils.I18n;

@SideOnly(Side.CLIENT)
public class GuiWaypointName extends GuiScreen
{
  private GuiTextField waypointName;

  private GuiButton doneButton;

  public GuiWaypointName(EntityPlayer entityPlayer)
  {
  }

  @Override
  public void updateScreen()
  {
    waypointName.updateCursorCounter();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void initGui()
  {
    Keyboard.enableRepeatEvents(true);
    buttonList.clear();
    buttonList.add(doneButton = new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("gui.done")));
    waypointName = new GuiTextField(fontRendererObj, this.width / 2 - 150, 60, 300, 20);
    waypointName.setMaxStringLength(128);
    waypointName.setFocused(true);
    waypointName.setText("");
    doneButton.enabled = waypointName.getText().trim().length() > 0;
  }

  @Override
  public void onGuiClosed()
  {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void actionPerformed(GuiButton par1GuiButton)
  {
    if (par1GuiButton.enabled)
    {
      PacketWaypointName packet = new PacketWaypointName(waypointName.getText());
      WarpBookMod.packetPipeline.sendToServer(packet);

      mc.displayGuiScreen((GuiScreen)null);
    }
  }

  @Override
  protected void keyTyped(char par1, int par2)
  {
    waypointName.textboxKeyTyped(par1, par2);
    doneButton.enabled = this.waypointName.getText().trim().length() > 0;

    if (!(par2 != 28 && par2 != 156))
    {
      actionPerformed(doneButton);
    }
  }

  @Override
  protected void mouseClicked(int par1, int par2, int par3)
  {
    super.mouseClicked(par1, par2, par3);
    waypointName.mouseClicked(par1, par2, par3);
  }

  @Override
  public void drawScreen(int par1, int par2, float par3)
  {
    drawDefaultBackground();
    drawCenteredString(fontRendererObj, I18n.format("warpbook.bindpage"), width / 2, 20, 16777215);
    drawString(fontRendererObj, I18n.format("warpbook.namewaypoint"), width / 2 - 150, 47, 10526880);
    waypointName.drawTextBox();
    super.drawScreen(par1, par2, par3);
  }
}
