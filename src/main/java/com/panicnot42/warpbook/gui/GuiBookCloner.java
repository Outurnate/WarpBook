package com.panicnot42.warpbook.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.panicnot42.warpbook.inventory.InventoryBookCloner;
import com.panicnot42.warpbook.inventory.container.ContainerBookCloner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiBookCloner extends GuiContainer {
	private static final ResourceLocation guiTexture = new ResourceLocation("warpbook", "textures/gui/cloneinv.png");
	
	private final InventoryBookCloner inventory;
	
	public GuiBookCloner(ContainerBookCloner bookClonerContainerItem) {
		super(bookClonerContainerItem);
		inventory = bookClonerContainerItem.inventory;
		xSize = 176;
		ySize = 180;
	}
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = inventory.getName();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTexture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
		super.keyTyped(c, keyCode);
		if (c == 1 || c == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
			mc.player.closeScreen();
		}
	}
	
}