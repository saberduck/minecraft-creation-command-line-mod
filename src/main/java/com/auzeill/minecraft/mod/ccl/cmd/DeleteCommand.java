package com.auzeill.minecraft.mod.ccl.cmd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".delete";
  }

  @Override
  public String getDescription() {
    return "Delete the saved areas. ex: .delete my-house";
  }

  @Override
  public void execute() {
    String fileName = argAsString(1);
    if (fileName != null) {
      try {
        Path filePath = SaveCommand.getSaveDir().resolve(fileName + SaveCommand.AREA_EXTENSION);
        if (Files.deleteIfExists(filePath)) {
          print(fileName + " deleted.");
        } else {
          print("Error, no file \"" + fileName + "\".");
        }
      } catch (IOException | RuntimeException e) {
        print(e.getClass().getSimpleName() + ": " + e.getMessage());
      }
    }
  }
}
