package panicnot42.warpbook.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import panicnot42.warpbook.CommonProxy;

public class ClientProxy extends CommonProxy
{
  @Override
  public void printMessage(String msg)
  {
    ChatComponentText chat = new ChatComponentText("\u00a76" + msg);
    Minecraft.getMinecraft().thePlayer.addChatMessage(chat);
  }

  @Override
  public void registerRenderers()
  {
  }
}
