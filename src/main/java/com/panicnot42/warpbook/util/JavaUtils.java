package com.panicnot42.warpbook.util;

import java.lang.reflect.Field;

public class JavaUtils {
	
	public static Object getRestrictedObject(Class clazz, Object from, String ... objNames) {
		for(String objName: objNames) {
			try {
				Field field = clazz.getDeclaredField(objName);
				field.setAccessible(true);
				return field.get(from);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) { }
		}
		
		return null;
	}
	
}
