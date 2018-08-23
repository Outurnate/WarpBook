package com.panicnot42.warpbook.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorableBlock {

    @SideOnly(Side.CLIENT)
	public int getColor(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex);
	
}
