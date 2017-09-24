package com.auzeill.minecraft.mod.ccl.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class BlockChange {

  public final BlockPos pos;
  public final IBlockState before;
  public final IBlockState after;

  public BlockChange(BlockPos pos, IBlockState before, IBlockState after) {
    this.pos = pos;
    this.before = before;
    this.after = after;
  }
}
