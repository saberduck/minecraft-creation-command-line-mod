package com.auzeill.minecraft.mod.ccl.world;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.util.math.BlockPos;

public class Area {

  public final BlockPos min;
  public final BlockPos max;

  public Area() {
    this.min = new BlockPos(0,0,0);
    this.max = min;
  }

  public Area(BlockPos pos) {
    this.min = pos;
    this.max = pos;
  }

  public Area(BlockPos p0, BlockPos p1) {
    this.min = new BlockPos(Math.min(p0.getX(), p1.getX()), Math.min(p0.getY(), p1.getY()), Math.min(p0.getZ(), p1.getZ()));
    this.max = new BlockPos(Math.max(p0.getX(), p1.getX()), Math.max(p0.getY(), p1.getY()), Math.max(p0.getZ(), p1.getZ()));
  }

  public Area(BlockPos p0, BlockPos p1, BlockPos p2) {
    this.min = new BlockPos(
      Math.min(Math.min(p0.getX(), p1.getX()), p2.getX()),
      Math.min(Math.min(p0.getY(), p1.getY()), p2.getY()),
      Math.min(Math.min(p0.getZ(), p1.getZ()), p2.getZ()));
    this.max = new BlockPos(
      Math.max(Math.max(p0.getX(), p1.getX()), p2.getX()),
      Math.max(Math.max(p0.getY(), p1.getY()), p2.getY()),
      Math.max(Math.max(p0.getZ(), p1.getZ()), p2.getZ()));
  }

  public Area addToMax(int x, int y, int z) {
    return new Area(min, new BlockPos(max.getX() + x, max.getY() + y, max.getZ() + z));
  }

  public Area addToMin(int x, int y, int z) {
    return new Area(new BlockPos(min.getX() + x, min.getY() + y, min.getZ() + z), max);
  }

  public int lengthX() {
    return max.getX() - min.getX() + 1;
  }

  public int lengthY() {
    return max.getY() - min.getY() + 1;
  }

  public int lengthZ() {
    return max.getZ() - min.getZ() + 1;
  }

  public int size() {
    return (max.getX() - min.getX()) * (max.getY() - min.getY()) * (max.getZ() - min.getZ());
  }

  public Area add(BlockPos pos) {
    return new Area(min, max, pos);
  }

  public Stream<BlockPos> allPositions() {
    return StreamSupport.stream(new AreaSpliterator(this), false);
  }

  @Override
  public String toString() {
    return min.toString() + "-" + max.toString();
  }

  static final class AreaSpliterator implements Spliterator<BlockPos> {

    private static final int CHARACTERISTICS = Spliterator.CONCURRENT |
      Spliterator.DISTINCT |
      Spliterator.IMMUTABLE |
      Spliterator.NONNULL |
      Spliterator.ORDERED |
      Spliterator.SIZED |
      Spliterator.SUBSIZED;

    private final BlockPos min;
    private final int lengthY;
    private final int lengthZ;
    private int index; // current index, modified on advance/split
    private final int fence; // one past last index

    public AreaSpliterator(Area area) {
      min = area.min;
      lengthY = area.lengthY();
      lengthZ = area.lengthZ();
      index = 0;
      fence = area.lengthX() * lengthY * lengthZ;
    }

    private AreaSpliterator(AreaSpliterator other, int index, int fence) {
      this.min = other.min;
      this.lengthY = other.lengthY;
      this.lengthZ = other.lengthZ;
      this.index = index;
      this.fence = fence;
    }

    @Override
    public void forEachRemaining(Consumer<? super BlockPos> action) {
      while (index < fence) {
        action.accept(blockAt(index));
        index++;
      }
    }

    public BlockPos blockAt(int index) {
      int y = index % lengthY;
      int r = index / lengthY;
      int z = r % lengthZ;
      int x = r / lengthZ;
      return new BlockPos(min.getX() + x, min.getY() + y, min.getZ() + z);
    }

    @Override
    public Comparator<? super BlockPos> getComparator() {
      return null;
    }

    @Override
    public boolean tryAdvance(Consumer<? super BlockPos> action) {
      if (index < fence) {
        action.accept(blockAt(index));
        index++;
        return true;
      }
      return false;
    }

    @Override
    public Spliterator<BlockPos> trySplit() {
      int otherIndex = index;
      int middle = (otherIndex + fence) >>> 1;
      if (otherIndex >= middle) {
        return null;
      }
      index = middle;
      return new AreaSpliterator(this, otherIndex, middle);
    }

    @Override
    public long estimateSize() {
      return (long) (fence - index);
    }

    @Override
    public int characteristics() {
      return CHARACTERISTICS;
    }
  }
}
