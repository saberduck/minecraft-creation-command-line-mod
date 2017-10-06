package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import net.minecraft.util.math.BlockPos;

public class PasteCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".paste";
  }

  @Override
  public String getDescription() {
    return "Paste the copied area from the clipboard. ex: .paste";
  }

  @Override
  public void execute() {
    BlockPos pos = pos();
    CopiedArea copiedArea = history().copiedArea;
    if (copiedArea != null && pos != null) {
      pasteCopiedArea(pos, player().getPosition(), copiedArea, addBlockChange(), false);
      print("Paste " + copiedArea.area.size() + " blocks.");
    }
  }

  public static void pasteCopiedArea(BlockPos pos, BlockPos playerPos, CopiedArea copiedArea, BlockChangeList change, boolean forceAir) {
    BlockPos relativePos = pos.subtract(playerPos);
    BlockPos destinationPos = pos;
    if (relativePos.getX() < 0) {
      destinationPos = destinationPos.add(1 - copiedArea.area.lengthX(), 0, 0);
    }
    if (relativePos.getY() > 0) {
      destinationPos = destinationPos.add(0, 1 - copiedArea.area.lengthY(), 0);
    }
    if (relativePos.getZ() < 0) {
      destinationPos = destinationPos.add(0, 0, 1 - copiedArea.area.lengthZ());
    }
    copiedArea.paste(change, destinationPos, forceAir);
  }

}
