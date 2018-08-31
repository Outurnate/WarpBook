package com.panicnot42.warpbook.core;

public enum WarpColors {
	
	UNBOUND(0xC3C36A, 0xC3C36A),//The default tawny color of a warp page
	BOUND(0x098C76, 0x6CF4DC),//The color of an ender pearl
	//0x9F1CE5 The original ugly purple player page
	PLAYER(0x8e0000, 0xE13232),//Red color.. blood maybe?
	HYPER(0x09AA38, 0x53FF41),//Hyper Green
	DEATHLY(0x131313, 0x686868),//Near Black
	LEATHER(0x654b17, 0x654b17);//The color of vanilla books
	
	private int	color;
	private int specColor;
	
	WarpColors(int color, int specColor) {
		this.color = color;
		this.specColor = specColor;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getSpecColor() {
		return specColor;
	}
}
