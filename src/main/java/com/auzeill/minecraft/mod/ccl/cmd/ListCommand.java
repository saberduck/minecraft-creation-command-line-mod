package com.auzeill.minecraft.mod.ccl.cmd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ListCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".list";
  }

  @Override
  public String getDescription() {
    return "List the saved areas. ex: .list";
  }

  @Override
  public void execute() {
    try {
      Path saveDir = SaveCommand.getSaveDir();
      if (!Files.exists(saveDir)) {
        print("<no saved area>");
      }
      Files.list(saveDir)
        .filter(path -> !Files.isDirectory(path) && path.getFileName().toString().endsWith(SaveCommand.AREA_EXTENSION))
        .map(Path::getFileName)
        .map(Path::toString)
        .map(name -> name.substring(0, name.length() - SaveCommand.AREA_EXTENSION.length()))
        .forEach(this::print);
    } catch (IOException | RuntimeException e) {
      print(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }
}
