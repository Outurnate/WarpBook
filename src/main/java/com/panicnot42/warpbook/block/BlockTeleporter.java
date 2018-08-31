package com.panicnot42.warpbook.block;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.core.WarpColors;
import com.panicnot42.warpbook.item.WarpPotionItem;
import com.panicnot42.warpbook.tileentity.TileEntityTeleporter;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTeleporter extends Block implements ITileEntityProvider, IColorableBlock {
	protected static final AxisAlignedBB TELEPORTER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 4.0D / 16.0D, 1.0D);
	
	public BlockTeleporter() {
		super(Material.IRON);
		setUnlocalizedName("teleporter");
		setRegistryName("teleporter");
		setCreativeTab(WarpBookMod.tabBook);
		setSoundType(SoundType.STONE);
		setHardness(10.0f);
		setResistance(20.0f);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTeleporter();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			IBlockState newState = state;
			TileEntityTeleporter teleporter = (TileEntityTeleporter)world.getTileEntity(pos);
			if (!player.getHeldItem(hand).isEmpty() && player.getHeldItem(hand).getItem() instanceof WarpPotionItem) {
				teleporter.setWarpItem(player.getHeldItem(hand));
				if (player.getHeldItem(hand).getCount() < 1) {
					player.getHeldItem(hand).shrink(1);
				}
				else {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
				}
				player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
			}
			world.notifyBlockUpdate(pos, state, newState, 3);
			teleporter.markDirty();
		}
		return true;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityPlayer) {
			ItemStack page = ((TileEntityTeleporter)worldIn.getTileEntity(pos)).getWarpItem();
			if (!page.isEmpty())
				WarpBookMod.warpDrive.handleWarp((EntityPlayer)entityIn, page);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return TELEPORTER_AABB;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
	public int getColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		TileEntityTeleporter teleporter = (TileEntityTeleporter)world.getTileEntity(pos);
		if(teleporter instanceof TileEntityTeleporter) {
			WarpColors wc = teleporter.getWarpColor();

			if(wc == WarpColors.UNBOUND) {
				return 0xFF888888;
			}

			switch (tintIndex) {
			default: return 0xFFFFFFFF;
			case 0: return wc.getColor(); //Base color
			case 1: return wc.getSpecColor(); //Specular color
			}
		}
		
		return 0xFFFFFFFF;
	}
	
}
