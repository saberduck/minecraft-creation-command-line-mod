package com.auzeill.minecraft.mod.ccl.city;

public class MinMaxPos {

  public final Pos min;

  public final Pos max;

  public MinMaxPos() {
    this.min = new Pos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    this.max = new Pos(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  public MinMaxPos(Pos min, Pos max) {
    this.min = min;
    this.max = max;
  }

  public MinMaxPos update(Pos pos) {
    if (min.x <= pos.x && max.x >= pos.x &&
      min.y <= pos.y && max.y >= pos.y &&
      min.z <= pos.z && max.z >= pos.z) {
      return this;
    }
    return new MinMaxPos(
      new Pos(Math.min(min.x, pos.x), Math.min(min.y, pos.y), Math.min(min.z, pos.z)),
      new Pos(Math.max(max.x, pos.x), Math.max(max.y, pos.y), Math.max(max.z, pos.z)));
  }

  public boolean isEmpty() {
    return min.x == Integer.MAX_VALUE;
  }

  public Pos size() {
    return new Pos(max.x - min.x + 1, max.y - min.y + 1, max.z - min.z + 1);
  }

}
