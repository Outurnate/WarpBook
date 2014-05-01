package panicnot42.warpbook;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class GuiWarpBookItemInventory extends GuiContainer
{
	private float xSize_lo;
	private float ySize_lo;
	
	private static final ResourceLocation iconLocation = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");
	
	private final WarpBookInventoryItem inventory;
	
	public GuiWarpBookItemInventory(WarpBookContainerItem warpBookContainerItem)
	{
		super(warpBookContainerItem);
		inventory = warpBookContainerItem.inventory;
		xSize = 176;
		ySize = 222;
	}

	public void drawScreen(int xsize, int ysize, float par3)
	{
		super.drawScreen(xsize, ysize, par3);
		this.xSize_lo = (float)xsize;
		this.ySize_lo = (float)ysize;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.getString(inventory.getInvName());
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(iconLocation);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
