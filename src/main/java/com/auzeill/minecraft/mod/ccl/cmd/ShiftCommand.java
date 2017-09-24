package com.auzeill.minecraft.mod.ccl.cmd;

import net.minecraft.util.math.Vec3i;

public class ShiftCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".shift";
  }

  @Override
  public String getDescription() {
    return "Shift the previously selected position by a value. ex: .shift x 2";
  }

  @Override
  public void execute() {
    Vec3i direction = direction(arg(1));
    Integer value = integer(arg(2));
    if (direction != null && value != null) {
      Vec3i shiftVec = multiply(direction, value);
      for (int i = 0; i < history().markedPos.size(); i++) {
        history().markedPos.set(i, history().markedPos.get(i).add(shiftVec));
      }
      print("Shifted by " + shiftVec.getX() + "x" + shiftVec.getY() + "x" + shiftVec.getZ());
    }
  }

}
