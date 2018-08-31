package com.panicnot42.warpbook.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WarpButton extends GuiButton {
	
	
    public WarpButton(int buttonId, int x, int y, String buttonText) {
    	super(buttonId, x, y, buttonText);
    }

    public WarpButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
    	super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int hoverState = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            int assetHeight = 20;
            int assetWidth = 200;
            int halfWidth = this.width / 2;
            int halfHeight = this.height / 2;

            //Upper Left
            this.drawTexturedModalRect(this.x, this.y, 0, (46 + hoverState * assetHeight), halfWidth, halfHeight);
            //Lower Left
            this.drawTexturedModalRect(this.x, this.y + halfHeight, 0, (46 + hoverState * assetHeight) + (assetHeight - halfHeight), halfWidth, halfHeight);
            //Upper Right
            this.drawTexturedModalRect(this.x + halfWidth, this.y, assetWidth - halfWidth, (46 + hoverState * assetHeight), halfWidth, halfHeight);
            //Lower Right
            this.drawTexturedModalRect(this.x + halfWidth, this.y + halfHeight, assetWidth - halfWidth, (46 + hoverState * assetHeight) + (assetHeight - halfHeight), halfWidth, halfHeight);            

            this.mouseDragged(mc, mouseX, mouseY);
            int textColor = 0xE0E0E0;

            if (packedFGColour != 0) {
                textColor = packedFGColour;
            }
            else
            if (!this.enabled) {
                textColor = 0xA0A0A0;
            }
            else if (this.hovered) {
                textColor = 0xFFFFA0;
            }
            
            if(this.displayString.equals("...") ) {
            	textColor = 0xFFFF44;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor);
        }
    }

}