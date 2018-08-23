package com.panicnot42.warpbook.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.inventory.InventoryWarpBook;
import com.panicnot42.warpbook.inventory.container.ContainerWarpBook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiWarpBookItemInventory extends GuiContainer {
	private static final ResourceLocation iconLocation = new ResourceLocation("warpbook", "textures/gui/warpinv.png");
	private static final ResourceLocation deathBox = new ResourceLocation("warpbook", "textures/gui/deathbox.png");
	
	private final InventoryWarpBook inventory;
	
	public GuiWarpBookItemInventory(ContainerWarpBook warpBookContainerItem) {
		super(warpBookContainerItem);
		inventory = warpBookContainerItem.inventory;
		xSize = 194;
		ySize = 222;
	}
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = inventory.getName();
		fontRenderer.drawString(s, (xSize - 18) / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(iconLocation);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		if (WarpBookMod.deathPagesEnabled) {
			mc.getTextureManager().bindTexture(deathBox);
			drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		}
	}
	
	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
		super.keyTyped(c, keyCode);
		if (c == 1 || c == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
			mc.player.closeScreen();
		}
	}
	
}
