package com.panicnot42.util;


public class CommandUtils
{
  public enum ChatType
  {
    TYPE_int, TYPE_player
  };

  private static final ChatStyle errorStyle;
  private static final ChatStyle usageStyle;
  private static final ChatStyle infoStyle;

  static
  {
    errorStyle = new ChatStyle();
    errorStyle.setColor(EnumChatFormatting.RED);

    usageStyle = new ChatStyle();
    usageStyle.setColor(EnumChatFormatting.YELLOW);

    infoStyle = new ChatStyle();
    infoStyle.setColor(EnumChatFormatting.WHITE);
  }

  public static void printUsage(ICommandSender sender, CommandBase createWaypointCommand)
  {
    ChatComponentText prefix = new ChatComponentText("Usage:"), usage = new ChatComponentText(createWaypointCommand.getCommandUsage(sender));
    prefix.setChatStyle(usageStyle);
    usage.setChatStyle(usageStyle);
    sender.addChatMessage(prefix);
    sender.addChatMessage(usage);
  }

  public static void showError(ICommandSender sender, ChatType type, String string)
  {
    showError(sender, String.format("'%s' is not a valid %s", string, getFriendlyName(type)));
  }

  public static void showError(ICommandSender sender, String message)
  {
    ChatComponentText text = new ChatComponentText(message);
    text.setChatStyle(errorStyle);
    sender.addChatMessage(text);
  }

  private static String getFriendlyName(ChatType type)
  {
    switch (type)
    {
      case TYPE_int:
        return "integer";
      case TYPE_player:
        return "player";
    }
    return "duck";
  }

  public static String stringConcat(String[] var2, int start)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = start; i < var2.length; ++i)
    {
      builder.append(var2[i]);
      builder.append(" ");
    }
    if (var2.length > 0) builder.deleteCharAt(builder.length() - 1); // remove
                                                                     // last
                                                                     // space
    return builder.toString();
  }

  public static void info(ICommandSender sender, String message)
  {
    ChatComponentText text = new ChatComponentText(message);
    text.setChatStyle(infoStyle);
    sender.addChatMessage(text);
  }
}
