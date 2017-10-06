package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.Server;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import com.auzeill.minecraft.mod.ccl.world.Serializer;
import com.auzeill.minecraft.mod.ccl.world.TickTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

public class WatchCommand extends ChatCommand {

  private List<TickTask> tasks = new ArrayList<>();

  @Override
  public String getName() {
    return ".watch";
  }

  @Override
  public String getDescription() {
    return "Automatically load, clear and paste the saved area each time the file has changed. Use \".watch stop\" to stop all.  ex: .watch my-house";
  }

  @Override
  public void execute() {
    String fileName = argAsString(1);
    if (fileName.equals("stop")) {
      tasks.forEach(TickTask::stop);
      tasks.clear();
      print("All watch stopped.");
      return;
    }
    BlockPos pos = pos();
    if (fileName.equals("server")) {
      Context context = new Context(pos, player().getPosition(), null, world(), player());
      tasks.add(TickTask.create(500, context::updateFromServer));
      print("Will automatically load using server");
      return;
    }
    if (fileName != null && pos != null) {
      Path filePath = SaveCommand.getSaveDir().resolve(fileName + SaveCommand.AREA_EXTENSION);
      if (Files.exists(filePath)) {
        Context context = new Context(pos, player().getPosition(), filePath, world(), player());
        tasks.add(TickTask.create(500, context::update));
        print("Will automatically load \"" + filePath + "\".");
      } else {
        print("Error, no file \"" + fileName + "\".");
      }
    }
  }

  private static class Context {

    private final BlockPos selectedPos;
    private final BlockPos playerPos;
    private final Path filePath;
    private final WorldServer world;
    private final EntityPlayerMP player;
    private FileTime fileModifiedDate = FileTime.from(0, TimeUnit.SECONDS);

    public Context(BlockPos selectedPos, BlockPos playerPos, Path filePath, WorldServer world, EntityPlayerMP player) {
      this.selectedPos = selectedPos;
      this.playerPos = playerPos;
      this.filePath = filePath;
      this.world = world;
      this.player = player;
    }

    public boolean update() {
      try {
        if (Files.exists(filePath)) {
          FileTime lastModifiedTime = Files.getLastModifiedTime(filePath);
          if (!fileModifiedDate.equals(lastModifiedTime)) {
            fileModifiedDate = lastModifiedTime;
            CopiedArea copiedArea = Serializer.deserializeCopiedArea(filePath);
            BlockChangeList change = new BlockChangeList(world);
            PasteCommand.pasteCopiedArea(selectedPos, playerPos, copiedArea, change, true);
            print("Loaded \"" + filePath + "\" " + copiedArea.area.size() + " blocks.");
          }
        }
      } catch (IOException | RuntimeException e) {
        print(e.getClass().getSimpleName() + ": " + e.getMessage());
      }
      return true;
    }

    public boolean updateFromServer() {
      Server server = Server.getInstance();
      CopiedArea area = server.queue.poll();
      if (area != null) {
        BlockChangeList change = new BlockChangeList(world);
        PasteCommand.pasteCopiedArea(selectedPos, playerPos, area, change, true);
      }
      return true;
    }

    public void print(String message) {
      player.sendMessage(new TextComponentString(TextFormatting.GREEN + message));
    }

  }

}
