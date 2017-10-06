package com.auzeill.minecraft.mod.ccl.city;

import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class World {

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
    MinMax xLimit = new MinMax();
    MinMax yLimit = new MinMax();
    MinMax zLimit = new MinMax();
    for (int x = 0; x < xWidth; x++) {
      for (int y = 0; y < yHeight; y++) {
        for (int z = 0; z < zDepth; z++) {
          if (!Block.Air.equals(blocks[x][y][z])) {
            xLimit.update(x);
            yLimit.update(y);
            zLimit.update(z);
          }
        }
      }
    }
    if (!xLimit.isEmpty()) {
      try (BufferedWriter code = Files.newWriter(path.toFile(), UTF_8)) {
        code.append("{\n");
        code.append("  \"area\": {\n");
        code.append("    \"min\": {\"x\": 0,\"y\": 0,\"z\": 0},\n");
        code.append("    \"max\": {");
        code.append("\"x\": ").append(Integer.toString(xLimit.size() - 1)).append(",");
        code.append("\"y\": ").append(Integer.toString(yLimit.size() - 1)).append(",");
        code.append("\"z\": ").append(Integer.toString(zLimit.size() - 1)).append("}\n");
        code.append("  },\n");
        code.append("  \"states\": [\n");
        code.append("    ");
        boolean firstElement = true;
        for (int x = xLimit.min(); x <= xLimit.max(); x++) {
          for (int z = zLimit.min(); z <= zLimit.max(); z++) {
            for (int y = yLimit.min(); y <= yLimit.max(); y++) {
              if (firstElement) {
                firstElement = false;
              } else {
                code.append(",\n    ");
              }
              code.append("\"").append(blocks[x][y][z]).append("\"");
            }
          }
        }
        code.append("\n");
        code.append("  ]\n");
        code.append("}\n");
      }
      System.out.println("(x: " + xLimit.size() + ", y: " + yLimit.size() + ", z: " + zLimit.size() + ")");
      System.out.println(path.getFileName() + " saved.");
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

}
