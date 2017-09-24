package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;

public class MergeCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".merge";
  }

  @Override
  public String getDescription() {
    return "Expand the selected area by using more than the two last selected point. ex: .merge 3";
  }

  @Override
  public void execute() {
    Area area = area();
    Integer mergeCount = integer(arg(1));
    if (mergeCount == null || mergeCount < 2) {
      print("Error: invalid merge count " + mergeCount + " .");
      return;
    }
    if (!markedPosHasAtLeast(mergeCount)) {
      print("Error: need " + mergeCount + " marked pos. Place more torch.");
      return;
    }
    for (int i = 2; i < mergeCount; i++) {
      area = area.add(history().markedPos.get(history().markedPos.size() - 1 - i));
    }
    history().markedPos.set(history().markedPos.size() - 2, area.min);
    history().markedPos.set(history().markedPos.size() - 1, area.max);
    area = area();
    print("Area merged (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
  }

}
