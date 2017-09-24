package com.auzeill.minecraft.mod.ccl.world;

import net.minecraft.util.math.BlockPos;

public class Area {

  public final BlockPos min;
  public final BlockPos max;

  public Area(BlockPos p0, BlockPos p1) {
    this.min = new BlockPos(Math.min(p0.getX(), p1.getX()), Math.min(p0.getY(), p1.getY()), Math.min(p0.getZ(), p1.getZ()));
    this.max = new BlockPos(Math.max(p0.getX(), p1.getX()), Math.max(p0.getY(), p1.getY()), Math.max(p0.getZ(), p1.getZ()));
  }

  public Area(BlockPos p0, BlockPos p1, BlockPos p2) {
    this.min = new BlockPos(
      Math.min(Math.min(p0.getX(), p1.getX()), p2.getX()),
      Math.min(Math.min(p0.getY(), p1.getY()), p2.getY()),
      Math.min(Math.min(p0.getZ(), p1.getZ()), p2.getZ()));
    this.max = new BlockPos(
      Math.max(Math.max(p0.getX(), p1.getX()), p2.getX()),
      Math.max(Math.max(p0.getY(), p1.getY()), p2.getY()),
      Math.max(Math.max(p0.getZ(), p1.getZ()), p2.getZ()));
  }

  public int lengthX() {
    return max.getX() - min.getX() + 1;
  }

  public int lengthY() {
    return max.getY() - min.getY() + 1;
  }

  public int lengthZ() {
    return max.getZ() - min.getZ() + 1;
  }

  public int size() {
    return (max.getX() - min.getX()) * (max.getY() - min.getY()) * (max.getZ() - min.getZ());
  }

  public Area add(BlockPos pos) {
    return new Area(min, max, pos);
  }

}
