package com.auzeill.minecraft.mod.ccl.city;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.CopiedArea;
import com.auzeill.minecraft.mod.ccl.world.Serializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Nice symbols that can be used in comment:
 * ┏ ┳ ━ ┓ ┣ ┃ ┫ ┗ ┻ ━ ┛
 * ┌   ─ ┐   │   └   ─ ┘
 *     ╌     ╎       ╌
 */
public class GenerateCity {

  /**
   * NORTH oriented house
   * it's a square
   * ╎          23           ╎
   * ╎    10    ╎3╎    10    ╎
   * ┌───────────────────────┐
   * │          ╎ ╎          │
   * │    ┏━━━━━───━━━━━┓    │
   * │    ┃             ┃    │
   * │    ┃             │╌╌╌╌│
   * │ 4  ┃             │╌╌╌╌│
   * │    ┃             ┃    │
   * │    ┗━━━━━━━━━━━━━┛    │
   * │          4            │
   * └───────────────────────┘
   *
   */

  static int HALF_GARDEN = 10;
  static int GARDEN_MARGIN = 4;
  static int HOUSE_PATH = 3;

  public static void main(String[] args) throws IOException {
    Bootstrap.register();
    // 0: 1 house, 1: 4 houses, 2: 16 houses, 2: 64 houses
    int fractalLevel = 2;
    int width = parcellingWidth(fractalLevel);
    CopiedArea out = new CopiedArea(new Area().addToMax(width - 1, 20, width - 1));
    drawParcelling(out, fractalLevel, new BlockPos(0, 0, 0), EnumFacing.EAST);
    saveToFile(out);
  }

  static int roadWidth(int level) {
    return level == 0 ? HOUSE_PATH : HOUSE_PATH + 2 * (level - 1);
  }

  static int parcellingWidth(int level) {
    int width = 2 * HALF_GARDEN + roadWidth(0);
    for (int i = 1; i <= level; i++) {
      width = 2 * width + roadWidth(i);
    }
    return width;
  }

  static void drawParcelling(CopiedArea out, int level, BlockPos pos, EnumFacing facing) {
    int width = parcellingWidth(level);
    Area area = new Area(pos, pos.add(width - 1, 0, width - 1));
    if (level == 0) {
      println("Draw House at " + area + " facing " + facing);
      drawHouse(out, pos, width, facing);
    } else {
      println("Draw Parcelling Level " + level + " at " + area + " facing " + facing);
      int subLevel = level - 1;
      int subWidth = parcellingWidth(subLevel);
      int roadWidth = roadWidth(level);
      drawParcelling(out, subLevel, pos, EnumFacing.WEST);
      drawParcelling(out, subLevel, new BlockPos(pos.getX(), pos.getY(), pos.getZ() + subWidth + roadWidth), EnumFacing.NORTH);
      drawParcelling(out, subLevel, new BlockPos(pos.getX() + subWidth + roadWidth, pos.getY(), pos.getZ() + subWidth + roadWidth), EnumFacing.EAST);
      drawParcelling(out, subLevel, new BlockPos(pos.getX() + subWidth + roadWidth, pos.getY(), pos.getZ()), EnumFacing.SOUTH);
      drawRoad(out, level, pos, roadWidth, width, facing.rotateY());
      drawRoad(out, level, pos, roadWidth, width, facing.rotateY().rotateY());
    }
  }

  static void drawHouse(CopiedArea out, BlockPos pos, int width, EnumFacing facing) {
    Area ground = new Area(pos).addToMax(width - 1, 0, width - 1);
    ground.allPositions().forEach(p -> out.set(p, Blocks.GRASS.getDefaultState()));

    IBlockState wallBlock = Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);

    Area wall_east = new Area(pos.add(width - 1 - GARDEN_MARGIN, 1, GARDEN_MARGIN)).addToMax(0, 4, width - 1 - 2 * GARDEN_MARGIN);
    wall_east.allPositions().forEach(p -> out.set(p, wallBlock));

    Area wall_south = new Area(pos.add(GARDEN_MARGIN, 1, GARDEN_MARGIN)).addToMax(width - 1 - 2 * GARDEN_MARGIN, 4, 0);
    wall_south.allPositions().forEach(p -> out.set(p, wallBlock));
    // TODO bug: replace wall_south by wall_east -> Dead stores should be removed

    Area wall_west = new Area(pos.add(GARDEN_MARGIN, 1, GARDEN_MARGIN)).addToMax(0, 4, width - 1 - 2 * GARDEN_MARGIN);
    wall_west.allPositions().forEach(p -> out.set(p, wallBlock));

    Area wall_north = new Area(pos.add(GARDEN_MARGIN, 1, width - 1 - GARDEN_MARGIN)).addToMax(width - 1 - 2 * GARDEN_MARGIN, 4, 0);
    wall_north.allPositions().forEach(p -> out.set(p, wallBlock));

  }

  static void drawRoad(CopiedArea out, int level, BlockPos pos, int roadWidth, int parcellingWidth, EnumFacing facing) {
    println("Draw road Level " + level + " at " + pos + " facing " + facing);
    IBlockState roadBlock = Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GRAY);
    if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
      BlockPos start = new BlockPos(pos.getX() + ((parcellingWidth - roadWidth) / 2), pos.getY(), pos.getZ());
      BlockPos end = new BlockPos(start.getX() + roadWidth - 1, pos.getY(), pos.getZ() + parcellingWidth - 1);
      Area road = new Area(start, end);
      road.allPositions().forEach(roadPos -> out.set(roadPos, roadBlock));
    } else {
      BlockPos start = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + ((parcellingWidth - roadWidth) / 2));
      BlockPos end = new BlockPos(pos.getX() + parcellingWidth - 1, pos.getY(), start.getZ() + roadWidth - 1);
      Area road = new Area(start, end);
      road.allPositions().forEach(roadPos -> out.set(roadPos, roadBlock));
    }
  }

  static void saveToFile(CopiedArea area) throws IOException {
    Path path = destinationFilePath();
    Serializer.serializeCopiedArea(path, area);
    println("Saved \"" + path.getFileName() + "\" " + area.area + ")");
  }

  static Path destinationFilePath() throws IOException {
    Path savedAreasDirectory = Paths.get("run/saved-areas");
    if (!savedAreasDirectory.toFile().exists()) {
      Files.createDirectory(savedAreasDirectory);
    }
    return savedAreasDirectory.resolve("city.area.json");
  }

  static void println(String text) {
    System.out.println(text);
  }
}
