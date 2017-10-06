package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import net.minecraft.init.Blocks;

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
      area.allPositions().forEach(pos -> change.setState(pos, Blocks.AIR.getDefaultState()));
      print("Clear success (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
