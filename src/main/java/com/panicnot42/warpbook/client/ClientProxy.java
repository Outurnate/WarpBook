package com.panicnot42.warpbook.client;

import com.panicnot42.warpbook.Proxy;
import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.item.IColorable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends Proxy {
	@Override
	public void registerRenderers() {
				
		//Misc
		regMesh(WarpBookMod.items.warpBookItem);
		regMesh(WarpBookMod.items.warpClusterItem);

		//Pages
		regMesh(WarpBookMod.items.unboundWarpPageItem);
		regMesh(WarpBookMod.items.locusWarpPageItem);
		regMesh(WarpBookMod.items.playerWarpPageItem);
		regMesh(WarpBookMod.items.hyperWarpPageItem);
		regMesh(WarpBookMod.items.deathlyWarpPageItem);
		for(int i = 0; i < 6; i++) {
			regMesh(WarpBookMod.items.legacyPageItem, i);
		}

		//Potions
		regMesh(WarpBookMod.items.unboundWarpPotionItem);
		regMesh(WarpBookMod.items.locusWarpPotionItem);
		regMesh(WarpBookMod.items.playerWarpPotionItem);
		regMesh(WarpBookMod.items.hyperWarpPotionItem);		
		
		regMesh(Item.getItemFromBlock(WarpBookMod.blocks.bookCloner));
		regMesh(Item.getItemFromBlock(WarpBookMod.blocks.teleporter));
	
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(
				new IBlockColor() {
					@Override
					public int colorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex) {
						return WarpBookMod.blocks.teleporter.getColor(state, access, pos, tintIndex);
					}
				}
				, new Block[] {WarpBookMod.blocks.teleporter}
			);
	}
	
	private void regMesh(Item item) {
		regMesh(item, 0);
	}
	
	private void regMesh(Item item, int meta) {
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		mesher.register(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		
		//Register Color Handler for the item.
		if(item instanceof IColorable) {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(
					new IItemColor() {
						@Override
						public int colorMultiplier(ItemStack stack, int tintIndex) {
							return ((IColorable) item).getColor(stack, tintIndex);
						}
						
					}
					, new Item[] {item}
				);
		}
	}
	
}
