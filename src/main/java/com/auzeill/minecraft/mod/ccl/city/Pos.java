package com.auzeill.minecraft.mod.ccl.city;

public class Pos {

  public static final Pos ZERO = new Pos(0, 0, 0);

  public final int x;
  public final int y;
  public final int z;

  public Pos(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Pos add(int dx, int dy, int dz) {
    return new Pos(x + dx, y + dy, z + dz);
  }

  @Override
  public String toString() {
    return "(x: " + x + ", y: " + y + ", z: " + z + ")";
  }
}
