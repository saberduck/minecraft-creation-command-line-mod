package com.auzeill.minecraft.mod.ccl.city;

import java.io.IOException;

public class UseBlock {

  public static void main(String[] args) throws IOException {
    World world = new World(100, 30, 100);

    world.set(0, 0, 0, Block.DiamondBlock);
    world.set(2, 0, 0, Block.Stone.Granite);
    world.set(3, 0, 0, Block.DiamondBlock);
    world.set(4, 0, 0, Block.Concrete.White);

    world.save("run/saved-areas/city.area.json");
  }

}
