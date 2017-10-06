package com.auzeill.minecraft.mod.ccl.city;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class World {

  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  private final int xWidth;
  private final int yHeight;
  private final int zDepth;
  private final String[][][] blocks;

  public World(int xWidth, int yHeight, int zDepth) {
    this.xWidth = xWidth;
    this.yHeight = yHeight;
    this.zDepth = zDepth;
    blocks = new String[xWidth][yHeight][zDepth];
    for (int x = 0; x < xWidth; x++) {
      for (int y = 0; y < yHeight; y++) {
        for (int z = 0; z < zDepth; z++) {
          blocks[x][y][z] = Block.Air;
        }
      }
    }
  }

  public String get(int x, int y, int z) {
    return blocks[x][y][z];
  }

  public void set(int x, int y, int z, String value) {
    blocks[x][y][z] = value;
  }

  public void save(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    MinMaxPos limit = new MinMaxPos();
    for (int x = 0; x < xWidth; x++) {
      for (int y = 0; y < yHeight; y++) {
        for (int z = 0; z < zDepth; z++) {
          if (!Block.Air.equals(blocks[x][y][z])) {
            limit = limit.update(new Pos(x, y, z));
          }
        }
      }
    }
    if (!limit.isEmpty()) {
      List<String> states = new ArrayList<>();
      for (int x = limit.min.x; x <= limit.max.x; x++) {
        for (int z = limit.min.z; z <= limit.max.z; z++) {
          for (int y = limit.min.y; y <= limit.max.y; y++) {
            states.add(blocks[x][y][z]);
          }
        }
      }
      Pos size = limit.size();
      try (BufferedWriter writer = java.nio.file.Files.newBufferedWriter(path, UTF_8)) {
        GSON.toJson(new SavedWorld(new Area(Pos.ZERO, size.add(-1, -1, -1)), states), writer);
      }
      System.out.println(path.getFileName() + " saved. Size: " + size);
    } else {
      System.out.println("Error, empty world.");
    }
  }

  private static class MinMax {

    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    public void update(int value) {
      if (min > value) {
        min = value;
      }
      if (max < value) {
        max = value;
      }
    }

    public int min() {
      return min;
    }

    public int max() {
      return max;
    }

    public boolean isEmpty() {
      if (min == Integer.MAX_VALUE) {
        return true;
      }
      return max - min < 0;
    }

    public int size() {
      return max - min + 1;
    }
  }

  private static class Area {
    public final Pos min;
    public final Pos max;

    public Area(Pos min, Pos max) {
      this.min = min;
      this.max = max;
    }
  }

  private static class SavedWorld {

    public final Area area;
    public final List<String> states;

    public SavedWorld(Area area, List<String> states) {
      this.area = area;
      this.states = states;
    }
  }

}
