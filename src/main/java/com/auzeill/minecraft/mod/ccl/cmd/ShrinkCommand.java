package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import net.minecraft.util.math.Vec3i;

public class ShrinkCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".shrink";
  }

  @Override
  public String getDescription() {
    return "Shrink the selected area. ex: .shrink x 3";
  }

  @Override
  public void execute() {
    Area area = area();
    Vec3i direction = direction(arg(1));
    Integer value = integer(arg(2));
    if (area != null && direction != null && value != null) {
      Vec3i shrinkVec = multiply(direction, value);
      Area newArea;
      if (shrinkVec.getX() >= 0 && shrinkVec.getY() >= 0 && shrinkVec.getZ() >= 0) {
        newArea = new Area(area.min, area.max.subtract(shrinkVec));
      } else {
        newArea = new Area(area.min.subtract(shrinkVec), area.max);
      }
      history().markedPos.set(history().markedPos.size() - 2, newArea.min);
      history().markedPos.set(history().markedPos.size() - 1, newArea.max);
      area = area();
      print("Area has been shrunk (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
