package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import com.auzeill.minecraft.mod.ccl.world.Serializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.util.math.BlockPos;

public class LoadCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".load";
  }

  @Override
  public String getDescription() {
    return "Load the saved area from a file to clipboard and paste it. ex: .load my-house";
  }

  @Override
  public void execute() {
    BlockPos pos = pos();
    String fileName = argAsString(1);
    if (fileName != null && pos != null) {
      Path filePath = SaveCommand.getSaveDir().resolve(fileName + SaveCommand.AREA_EXTENSION);
      if (Files.exists(filePath)) {
        try {
          CopiedArea copiedArea = Serializer.deserializeCopiedArea(filePath);
          PasteCommand.pasteCopiedArea(pos, player().getPosition(), copiedArea, addBlockChange(), false);
          history().copiedArea = copiedArea;
          print("Loaded \"" + filePath + "\" " + copiedArea.area.size() + " blocks.");
        } catch (IOException | RuntimeException e) {
          print(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
      } else {
        print("Error, no file \"" + fileName + "\".");
      }
    }
  }

}
