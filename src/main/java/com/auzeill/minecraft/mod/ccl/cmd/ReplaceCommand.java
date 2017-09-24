package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReplaceCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".replace";
  }

  @Override
  public String getDescription() {
    return "Replace blocks in the selected area that match the one held in the off hand by the one held main hand. ex: .replace";
  }

  @Override
  public void execute() {
    Area area = area();
    World world = world();
    IBlockState offBlockState = offHandHeldBlockState();
    IBlockState mainBlockState = mainHandHeldBlockState();
    if (area != null && offBlockState != null && mainBlockState != null) {
      int changed = 0;
      BlockChangeList change = addBlockChange();
      for (int x = area.min.getX(); x <= area.max.getX(); x++) {
        for (int y = area.min.getY(); y <= area.max.getY(); y++) {
          for (int z = area.min.getZ(); z <= area.max.getZ(); z++) {
            BlockPos pos = new BlockPos(x, y, z);
            if (world.getBlockState(pos).equals(offBlockState)) {
              change.setState(pos, mainBlockState);
              changed++;
            }
          }
        }
      }
      print("Changed " + changed + " blocks.");
    }
  }

}
