package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NoWaterCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".nowater";
  }

  @Override
  public String getDescription() {
    return "Remove the water in the selected area. ex: .nowater";
  }

  @Override
  public void execute() {
    Area area = area();
    World world = world();
    if (area != null) {
      BlockChangeList change = addBlockChange();
      for (int x = area.min.getX(); x <= area.max.getX(); x++) {
        for (int y = area.min.getY(); y <= area.max.getY(); y++) {
          for (int z = area.min.getZ(); z <= area.max.getZ(); z++) {
            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos).getBlock().equals(Blocks.WATER)) {
              change.setState(pos, Blocks.AIR.getDefaultState());
            }
          }
        }
      }
      print("Removed water.");
    }
  }

}
