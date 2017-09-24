package com.auzeill.minecraft.mod.ccl.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CopiedArea {

  public final Area area;
  public final List<IBlockState> states;

  public CopiedArea(World world, Area area) {
    this.area = area;
    states = new ArrayList<>();
    for (int x = area.min.getX(); x <= area.max.getX(); x++) {
      for (int z = area.min.getZ(); z <= area.max.getZ(); z++) {
        for (int y = area.min.getY(); y <= area.max.getY(); y++) {
          states.add(world.getBlockState(new BlockPos(x, y, z)));
        }
      }
    }
  }

  private CopiedArea(Area area, List<IBlockState> states) {
    this.area = area;
    this.states = states;
  }

  public void paste(BlockChangeList change, BlockPos pos) {
    partialPaste(change, pos, true);
    partialPaste(change, pos, false);
  }

  public void partialPaste(BlockChangeList change, BlockPos pos, boolean topSolid) {
    int dx = pos.getX() - area.min.getX();
    int dy = pos.getY() - area.min.getY();
    int dz = pos.getZ() - area.min.getZ();
    int i = 0;
    for (int x = area.min.getX(); x <= area.max.getX(); x++) {
      for (int z = area.min.getZ(); z <= area.max.getZ(); z++) {
        for (int y = area.min.getY(); y <= area.max.getY(); y++) {
          IBlockState state = states.get(i);
          if (!state.getBlock().equals(Blocks.AIR) && topSolid == state.getBlock().isTopSolid(state)) {
            change.setState(new BlockPos(x + dx, y + dy, z + dz), state);
          }
          i++;
        }
      }
    }
  }

  public CopiedArea rotate() {
    int lengthX = area.lengthX();
    int lengthY = area.lengthY();
    int lengthZ = area.lengthZ();

    Area newArea = new Area(
      new BlockPos(area.min.getX(), area.min.getY(), area.min.getZ()),
      new BlockPos(area.min.getX() + lengthZ - 1, area.max.getY(), area.min.getZ() + lengthX - 1));

    List<IBlockState> newStates = new ArrayList<>(states.size());
    newStates.addAll(states);
    for (int x = 0; x < lengthX; x++) {
      for (int z = 0; z < lengthZ; z++) {
        for (int y = 0; y < lengthY; y++) {
          int oldIndex = (x * lengthZ + z) * lengthY + y;
          int newIndex = ((lengthZ - z - 1) * lengthX + x) * lengthY + y;
          IBlockState state = states.get(oldIndex);
          newStates.set(newIndex, state.withRotation(Rotation.CLOCKWISE_90));
        }
      }
    }
    return new CopiedArea(newArea, newStates);
  }

  public CopiedArea mirror(EnumFacing.Axis axis) {
    int lengthX = area.lengthX();
    int lengthY = area.lengthY();
    int lengthZ = area.lengthZ();
    List<IBlockState> newStates = new ArrayList<>(states.size());
    newStates.addAll(states);
    for (int x = 0; x < lengthX; x++) {
      for (int z = 0; z < lengthZ; z++) {
        for (int y = 0; y < lengthY; y++) {
          int oldIndex = (x * lengthZ + z) * lengthY + y;
          IBlockState state = states.get(oldIndex);
          EnumFacing facing = facing(state);
          int newIndex;
          switch (axis) {
            case X:
              newIndex = ((lengthX - x - 1) * lengthZ + z) * lengthY + y;
              if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                state = state.withRotation(Rotation.CLOCKWISE_180);
              }
              break;
            case Y:
              newIndex = (x * lengthZ + z) * lengthY + (lengthY - y - 1);
              break;
            case Z:
              newIndex = (x * lengthZ + (lengthZ - z - 1)) * lengthY + y;
              if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                state = state.withRotation(Rotation.CLOCKWISE_180);
              }
              break;
            default:
              throw new UnsupportedOperationException();
          }
          newStates.set(newIndex, state);
        }
      }
    }
    return new CopiedArea(area, newStates);
  }

  private static EnumFacing facing(IBlockState state) {
    for (Comparable<?> comparable : state.getProperties().values()) {
      if (comparable instanceof EnumFacing) {
        return (EnumFacing) comparable;
      }
    }
    return null;
  }

}
