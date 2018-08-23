package com.panicnot42.warpbook.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.panicnot42.warpbook.Properties;
import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.IDeclareWarp;
import com.panicnot42.warpbook.net.packet.PacketWarp;
import com.panicnot42.warpbook.util.CommandUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen {
	private final EntityPlayer entityPlayer;
	private NBTTagList items;
	private static final ResourceLocation invBg = new ResourceLocation(Properties.modid, "textures/gui/book.png");
	private int xSize, ySize, page, pageCount;
	private GuiButton next, prev;
	private ArrayList<WarpButton> warps;
	private ArrayList<ButtonPos> pos;
	private static final int warpsPerPage = 10;
	
	private class ButtonPos {
		public int id;
		public String name;
		
		public ButtonPos(int id, String name) {
			this.id = id;
			this.name = name;
		}
	};
	
	public GuiBook(EntityPlayer entityPlayer) {
		this.entityPlayer = entityPlayer;
	}
		
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		xSize = 146;
		ySize = 180;
		page = 0;
		ItemStack heldItem = entityPlayer.getHeldItemMainhand();
		if (!heldItem.hasTagCompound()) {
			heldItem.setTagCompound(new NBTTagCompound());
		}
		items = heldItem.getTagCompound().getTagList("WarpPages", new NBTTagCompound().getId());
		if (items.tagCount() == 0) {
			CommandUtils.showError(entityPlayer, I18n.format("help.nopages"));
			mc.displayGuiScreen((GuiScreen)null);
			return;
		}
		pos = new ArrayList<ButtonPos>();
		for (int i = 0; i < items.tagCount(); ++i) {
			ItemStack stack = new ItemStack(items.getCompoundTagAt(i));
			if (stack.getItem() instanceof IDeclareWarp ) {
				if( ((IDeclareWarp)stack.getItem()).hasValidData(stack)) {
					pos.add(new ButtonPos(i, ((IDeclareWarp)stack.getItem()).getName(entityPlayer.getEntityWorld(), stack)));
				} else {
					pos.add(new ButtonPos(i, "..."));
				}
			}
		}
		
		int x = width / 2 - 48;
		int y = 12 + (height / 2) - (ySize / 2);
		int n = Math.min(warpsPerPage, pos.size());
		warps = new ArrayList<WarpButton>();
		for (int i = 0; i < n; ++i) {
			WarpButton but = new WarpButton(n, x, y + (14 * i), 96, 12, "");
			buttonList.add(but);
			warps.add(but);
		}
		y = (height / 2) + (ySize / 2) - 24;
		
		// Add back and forward buttons
		pageCount = (pos.size() - 1) / (warpsPerPage);
		if (pageCount != 0) {
			buttonList.add(prev = new NextPageButton(64, x, y, false));
			buttonList.add(next = new NextPageButton(65, x + 64, y, true));
		}
		
		updateButtonStat();
	}

	private void updateButtonStat() {
		page = MathHelper.clamp(page, 0, pageCount);
		
		if (prev != null) {
			prev.visible = page != 0;
		}
		if (next != null) {
			next.visible = pageCount > page;
		}
		int r = pos.size() - page * warpsPerPage;
		int n = Math.min(r, warpsPerPage);
		
		//Hide all of the warp buttons
		for(WarpButton wb : warps) {
			wb.visible = false;
		}
		
		for (int i = 0; i < n; i++) {
			int j = page * warpsPerPage + i;
			WarpButton warp = warps.get(i);
			warp.id = pos.get(j).id;
			warp.displayString = pos.get(j).name;
			warp.visible = true;
		}
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	protected void actionPerformed(GuiButton guiButton) {
		switch (guiButton.id) {
		case 64:
			--page;
			updateButtonStat();
			break;
		case 65:
			++page;
			updateButtonStat();
			break;
		default:
			PacketWarp packet = new PacketWarp(guiButton.id);
			ItemStack page = PacketWarp.getPageById(entityPlayer, guiButton.id);
			WarpBookMod.warpDrive.handleWarp(Minecraft.getMinecraft().player, page);
			WarpBookMod.network.sendToServer(packet);
			mc.displayGuiScreen((GuiScreen)null);
			break;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(invBg);
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 20, 1, xSize, ySize);
		drawCenteredString(fontRenderer, I18n.format("warpbook.dowarp"), width / 2, (height / 2) - ySize / 2 - 12, 0xFFFFFF);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
        {
            private final boolean isForward;

            public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
            {
                super(buttonId, x, y, 23, 13, "");
                this.isForward = isForwardIn;
            }

            /**
             * Draws this button to the screen.
             */
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
            {
                if (this.visible)
                {
                    boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            		mc.getTextureManager().bindTexture(invBg);
                    int i = 0;
                    int j = 192;

                    if (flag)
                    {
                        i += 23;
                    }

                    if (!this.isForward)
                    {
                        j += 13;
                    }

                    this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
                }
            }
        }
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
		
		super.keyTyped(c, keyCode);
		switch(keyCode) {
			case 203: page--; updateButtonStat(); break;
			case 205: page++; updateButtonStat(); break;
		}
		
		if (c == 1 || c == 'e') {// if the player rebinds, this won't work. I don't care
			mc.player.closeScreen();
		}
	}
}
