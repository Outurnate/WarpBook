package com.panicnot42.warpbook.util;

public class StringUtils {
	public static String shorten(String string, int length) {
		if (string.length() > length) {
			StringBuilder builder = new StringBuilder();
			builder.append(string.substring(0, length - 4));
			builder.append("...");
			return builder.toString();
		}
		return string;
	}
	
}
