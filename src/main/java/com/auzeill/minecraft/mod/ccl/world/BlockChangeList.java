package com.auzeill.minecraft.mod.ccl.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockChangeList implements Change {

  private final World world;
  public final List<BlockChange> changes;

  public BlockChangeList(World world) {
    this.world = world;
    this.changes = new ArrayList<>();
  }

  public void setState(BlockPos pos, IBlockState newState) {
    IBlockState before = world.getBlockState(pos);
    world.setBlockState(pos, newState);
    changes.add(new BlockChange(pos, before, newState));
  }

  public void execute() {
    for (BlockChange change : changes) {
      world.setBlockState(change.pos, change.after);
    }
  }

  public void undo() {
    for (int i = changes.size() - 1; i >= 0; i--) {
      BlockChange change = changes.get(i);
      world.setBlockState(change.pos, change.before);
    }
  }

}
