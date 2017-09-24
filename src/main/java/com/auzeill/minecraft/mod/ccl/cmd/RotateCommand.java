package com.auzeill.minecraft.mod.ccl.cmd;

public class RotateCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".rot";
  }

  @Override
  public String getDescription() {
    return "Rotate clockwise the copied area in the clipboard. ex: .rot";
  }

  @Override
  public void execute() {
    if (history().copiedArea != null) {
      history().copiedArea = history().copiedArea.rotate();
      print("Rotated.");
    } else {
      print("ERROR, no copied area.");
    }
  }

}
