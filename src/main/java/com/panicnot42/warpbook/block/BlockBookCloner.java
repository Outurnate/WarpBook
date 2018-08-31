package com.panicnot42.warpbook.block;

import com.panicnot42.warpbook.WarpBookMod;
import com.panicnot42.warpbook.tileentity.TileEntityBookCloner;
import com.panicnot42.warpbook.util.WorldUtils;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBookCloner extends BlockContainer {
	
	protected static final AxisAlignedBB BOOKCLONER_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
	
	public BlockBookCloner() {
		super(Material.IRON);
		setUnlocalizedName("bookcloner");
		setRegistryName("bookcloner");
		setCreativeTab(WarpBookMod.tabBook);
		setSoundType(SoundType.STONE);
		setHardness(10.0f);
		setResistance(20.0f);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityBookCloner();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		player.openGui(WarpBookMod.instance, WarpBookMod.BookClonerInventoryGuiIndex, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOOKCLONER_AABB;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			ItemStack template = ((TileEntityBookCloner)world.getTileEntity(pos)).getTemplate();
			ItemStack pages = ((TileEntityBookCloner)world.getTileEntity(pos)).getPages();
			ItemStack cover = ((TileEntityBookCloner)world.getTileEntity(pos)).getCover();
			if (!template.isEmpty()) {
				WorldUtils.dropItemStackInWorld(world, pos.getX(), pos.getY(), pos.getZ(), template);
			}
			if (!pages.isEmpty()) {
				WorldUtils.dropItemStackInWorld(world, pos.getX(), pos.getY(), pos.getZ(), pages);
			}
			if (!cover.isEmpty()) {
				WorldUtils.dropItemStackInWorld(world, pos.getX(), pos.getY(), pos.getZ(), cover);
			}
		}
	}
	
}
