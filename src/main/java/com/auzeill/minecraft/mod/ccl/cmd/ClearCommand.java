package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ClearCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".clear";
  }

  @Override
  public String getDescription() {
    return "Replace the selected area by air. ex: .clear";
  }

  @Override
  public void execute() {
    Area area = area();
    if (area != null) {
      BlockChangeList change = addBlockChange();
      for (int x = area.min.getX(); x <= area.max.getX(); x++) {
        for (int y = area.min.getY(); y <= area.max.getY(); y++) {
          for (int z = area.min.getZ(); z <= area.max.getZ(); z++) {
            change.setState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
          }
        }
      }
      print("Clear success (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
