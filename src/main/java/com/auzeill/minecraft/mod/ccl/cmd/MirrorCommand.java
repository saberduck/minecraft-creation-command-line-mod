package com.auzeill.minecraft.mod.ccl.cmd;

import net.minecraft.util.EnumFacing;

public class MirrorCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".mirror";
  }

  @Override
  public String getDescription() {
    return "Mirror the copied area in the clipboard. ex: .mirror";
  }

  @Override
  public void execute() {
    EnumFacing.Axis axis = axisArgument(1);
    if (history().copiedArea != null) {
      history().copiedArea = history().copiedArea.mirror(axis);
      print("Mirrored.");
    } else {
      print("ERROR, no copied area.");
    }
  }

}
