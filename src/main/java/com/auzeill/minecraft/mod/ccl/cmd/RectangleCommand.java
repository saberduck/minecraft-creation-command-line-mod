package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class RectangleCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".rec";
  }

  @Override
  public String getDescription() {
    return "Create a rectangle around the selected area with block held in the main hand. ex: .rec";
  }

  @Override
  public void execute() {
    Area area = area();
    IBlockState blockState = mainHandHeldBlockState();
    if (area != null && blockState != null) {
      BlockChangeList change = addBlockChange();
      for (int x = area.min.getX(); x <= area.max.getX(); x++) {
        change.setState(new BlockPos(x, area.min.getY(), area.min.getZ()), blockState);
        change.setState(new BlockPos(x, area.min.getY(), area.max.getZ()), blockState);
        change.setState(new BlockPos(x, area.max.getY(), area.min.getZ()), blockState);
        change.setState(new BlockPos(x, area.max.getY(), area.max.getZ()), blockState);
      }
      for (int y = area.min.getY() + 1; y <= area.max.getY() - 1; y++) {
        change.setState(new BlockPos(area.min.getX(), y, area.min.getZ()), blockState);
        change.setState(new BlockPos(area.min.getX(), y, area.max.getZ()), blockState);
        change.setState(new BlockPos(area.max.getX(), y, area.min.getZ()), blockState);
        change.setState(new BlockPos(area.max.getX(), y, area.max.getZ()), blockState);
      }
      for (int z = area.min.getZ() + 1; z <= area.max.getZ() - 1; z++) {
        change.setState(new BlockPos(area.min.getX(), area.min.getY(), z), blockState);
        change.setState(new BlockPos(area.min.getX(), area.max.getY(), z), blockState);
        change.setState(new BlockPos(area.max.getX(), area.min.getY(), z), blockState);
        change.setState(new BlockPos(area.max.getX(), area.max.getY(), z), blockState);
      }
      print("Rectangle created (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
