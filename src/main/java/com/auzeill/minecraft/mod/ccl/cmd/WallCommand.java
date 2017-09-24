package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class WallCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".wall";
  }

  @Override
  public String getDescription() {
    return "Create a wall around the selected area with block held in the main hand. ex .wall";
  }

  @Override
  public void execute() {
    Area area = area();
    IBlockState blockState = mainHandHeldBlockState();
    if (area != null && blockState != null) {
      BlockChangeList change = addBlockChange();
      for (int x = area.min.getX(); x <= area.max.getX(); x++) {
        for (int y = area.min.getY(); y <= area.max.getY(); y++) {
          change.setState(new BlockPos(x, y, area.min.getZ()), blockState);
          change.setState(new BlockPos(x, y, area.max.getZ()), blockState);
        }
      }
      for (int y = area.min.getY(); y <= area.max.getY(); y++) {
        for (int z = area.min.getZ(); z <= area.max.getZ(); z++) {
          change.setState(new BlockPos(area.min.getX(), y, z), blockState);
          change.setState(new BlockPos(area.max.getX(), y, z), blockState);
        }
      }
      print("Wall created (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
