package com.panicnot42.warpbook.util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandUtils {
	public enum ChatType {
		TYPE_int, TYPE_player
	};
	
	private static final Style errorStyle;
	private static final Style usageStyle;
	private static final Style infoStyle;
	
	static {
		errorStyle = new Style();
		errorStyle.setColor(TextFormatting.RED);
		
		usageStyle = new Style();
		usageStyle.setColor(TextFormatting.YELLOW);
		
		infoStyle = new Style();
		infoStyle.setColor(TextFormatting.WHITE);
	}
	
	public static void printUsage(ICommandSender sender, CommandBase createWaypointCommand) {
		TextComponentString prefix = new TextComponentString("Usage:"), usage = new TextComponentString(createWaypointCommand.getUsage(sender));
		prefix.setStyle(usageStyle);
		usage.setStyle(usageStyle);
		sender.sendMessage(prefix);
		sender.sendMessage(usage);
	}
	
	public static void showError(ICommandSender sender, ChatType type, String string) {
		showError(sender, String.format("'%s' is not a valid %s", string, getFriendlyName(type)));
	}
	
	public static void showError(ICommandSender sender, String message) {
		TextComponentString text = new TextComponentString(message);
		text.setStyle(errorStyle);
		sender.sendMessage(text);
	}
	
	private static String getFriendlyName(ChatType type) {
		switch (type) {
		case TYPE_int:
			return "integer";
		case TYPE_player:
			return "player";
		}
		return "duck";
	}
	
	public static String stringConcat(String[] var2, int start) {
		StringBuilder builder = new StringBuilder();
		for (int i = start; i < var2.length; ++i) {
			builder.append(var2[i]);
			builder.append(" ");
		}
		if (var2.length > 0) {
			builder.deleteCharAt(builder.length() - 1); // remove
		}
		// last
		// space
		return builder.toString();
	}
	
	public static void info(ICommandSender sender, String message) {
		TextComponentBase text = new TextComponentString(message);
		text.setStyle(infoStyle);
		sender.sendMessage(text);
	}
	
}
