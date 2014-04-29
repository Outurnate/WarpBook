package panicnot42.warpbook.client;

import net.minecraft.client.Minecraft;
import panicnot42.warpbook.CommonProxy;

public class ClientProxy extends CommonProxy
{
	@Override
	public void printMessage(String msg)
	{
		Minecraft.getMinecraft().thePlayer.addChatMessage("\u00a76" + msg);
	}
	
	@Override
	public void registerRenderers()
	{
	}
}
