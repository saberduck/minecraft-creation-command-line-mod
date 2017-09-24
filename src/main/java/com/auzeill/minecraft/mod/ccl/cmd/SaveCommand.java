package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import com.auzeill.minecraft.mod.ccl.world.Serializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraft.client.Minecraft.getMinecraft;

public class SaveCommand extends ChatCommand {

  public static final String AREA_EXTENSION = ".area.json";

  @Override
  public String getName() {
    return ".save";
  }

  @Override
  public String getDescription() {
    return "Save the selected area in the clipboard and a file. ex: .save my-house";
  }

  @Override
  public void execute() {
    String fileName = argAsString(1);
    Area area = area();
    if (fileName != null && area != null) {
      try {
        Path saveDir = getSaveDir();
        if (!Files.exists(saveDir)) {
          Files.createDirectory(saveDir);
        }
        String sanitizedName = fileName.replaceAll("[^ ()+,\\-.0-9@A-Z\\[\\]_a-z{}\\u0080-\\uffff]", "_");
        Path filePath = saveDir.resolve(sanitizedName + AREA_EXTENSION);
        CopiedArea copiedArea = new CopiedArea(world(), area);
        history().copiedArea = copiedArea;
        Serializer.serializeCopiedArea(filePath, copiedArea);
        print("Saved \"" + sanitizedName + "\" (Size: " + area.lengthX() + "x" + area.lengthY() + "x" + area.lengthZ() + ")");
      } catch (IOException | RuntimeException e) {
        print(e.getClass().getSimpleName() + ": " + e.getMessage());
      }
    }
  }

  public static Path getSaveDir() {
    return getMinecraft().mcDataDir.toPath().resolve("saved-areas");
  }

}
