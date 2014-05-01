package panicnot42.warpbook;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntityCommandBlock;

import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiWaypointName extends GuiScreen
{
    private GuiTextField waypointName;

    private GuiButton doneButton;
    
    private final EntityPlayer entityPlayer;

    public GuiWaypointName(EntityPlayer entityPlayer)
    {
    	this.entityPlayer = entityPlayer;
    }

    @Override
    public void updateScreen()
    {
        waypointName.updateCursorCounter();
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(doneButton = new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.getString("gui.done")));
        waypointName = new GuiTextField(fontRenderer, this.width / 2 - 150, 60, 300, 20);
        waypointName.setMaxStringLength(10);
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
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	DataOutputStream outputStream = new DataOutputStream(bos);
        	try
        	{
        		outputStream.write(waypointName.getText().getBytes("ASCII"));
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        	Packet250CustomPayload packet = new Packet250CustomPayload();
        	packet.channel = "WarpBookWaypoint";
        	packet.data = bos.toByteArray();
        	packet.length = bos.size();
        	((EntityClientPlayerMP)entityPlayer).sendQueue.addToSendQueue(packet);

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
        drawCenteredString(fontRenderer, I18n.getString("warpbook.bindpage"), width / 2, 20, 16777215);
        drawString(fontRenderer, I18n.getString("warpbook.namewaypoint"), width / 2 - 150, 47, 10526880);
        waypointName.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
