package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import net.minecraft.util.math.Vec3i;

public class ExpandCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".expand";
  }

  @Override
  public String getDescription() {
    return "Expand the selected area. ex: .expand y 50";
  }

  @Override
  public void execute() {
    Area area = area();
    Vec3i direction = direction(arg(1));
    Integer value = integer(arg(2));
    if (area != null && direction != null && value != null) {
      Vec3i expandVec = multiply(direction, value);
      Area newArea;
      if (expandVec.getX() >= 0 && expandVec.getY() >= 0 && expandVec.getZ() >= 0) {
        newArea = new Area(area.min, area.max.add(expandVec));
      } else {
        newArea = new Area(area.min.add(expandVec), area.max);
      }
      history().markedPos.set(history().markedPos.size() - 2, newArea.min);
      history().markedPos.set(history().markedPos.size() - 1, newArea.max);
      area = area();
      print("Area expanded (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
