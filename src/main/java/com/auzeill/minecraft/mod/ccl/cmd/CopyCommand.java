package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import com.auzeill.minecraft.mod.ccl.world.Area;

public class CopyCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".copy";
  }

  @Override
  public String getDescription() {
    return "Copy the selected area in the clipboard. ex: .copy";
  }

  @Override
  public void execute() {
    Area area = area();
    if (area != null) {
      history().copiedArea = new CopiedArea(world(), area);
      print("Copied (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
    }
  }

}
