package panicnot42.warpbook;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.packet.Packet250CustomPayload;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen
{
    private final EntityPlayer entityPlayer;
    private NBTTagList items;

    public GuiBook(EntityPlayer entityPlayer)
    {
    	this.entityPlayer = entityPlayer;
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        if (!entityPlayer.getHeldItem().hasTagCompound())
        	entityPlayer.getHeldItem().setTagCompound(new NBTTagCompound());
        items = entityPlayer.getHeldItem().getTagCompound().getTagList("WarpPages");
        if (items.tagCount() == 0)
        {
        	WarpBook.proxy.printMessage("There are no pages in this book.  Shift+right click to add bound pages");
          mc.displayGuiScreen((GuiScreen)null);
          return;
        }
    	for (int i = 0; i < items.tagCount(); ++i)
    	{
    		NBTTagCompound compound = ItemStack.loadItemStackFromNBT((NBTTagCompound)items.tagAt(i)).getTagCompound();
    		try
    		{
    			buttonList.add(new GuiButton(i, ((width - 404) / 2) + ((i % 6) * 68), 16 + (24 * (i / 6)), 64, 16, compound.getString("name")));
    		}
    		catch (Exception e)
    		{
    			//old page
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
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	DataOutputStream outputStream = new DataOutputStream(bos);
    	try
    	{
    		outputStream.writeInt(guiButton.id);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	Packet250CustomPayload packet = new Packet250CustomPayload();
    	packet.channel = "WarpBookWarp";
    	packet.data = bos.toByteArray();
    	packet.length = bos.size();
    	((EntityClientPlayerMP)entityPlayer).sendQueue.addToSendQueue(packet);
    	
      //mc.displayGuiScreen((GuiScreen)null);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, I18n.getString("warpbook.dowarp"), width / 2, 6, 16777215);
        super.drawScreen(par1, par2, par3);
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
      return false;
    }
}
