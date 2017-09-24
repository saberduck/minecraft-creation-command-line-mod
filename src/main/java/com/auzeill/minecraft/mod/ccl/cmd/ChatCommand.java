package com.auzeill.minecraft.mod.ccl.cmd;

import com.auzeill.minecraft.mod.ccl.world.Area;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import com.auzeill.minecraft.mod.ccl.world.PlayerHistory;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ServerChatEvent;

import static java.util.Locale.ROOT;

public abstract class ChatCommand {

  private ServerChatEvent event;

  private PlayerHistory history;

  private List<ChatCommand> commands;

  public abstract String getName();

  public abstract String getDescription();

  public boolean accept(String message) {
    return message.startsWith(getName()) &&
      (message.length() == getName().length() || message.charAt(getName().length()) == ' ');
  }

  public void execute(ServerChatEvent event, PlayerHistory history, List<ChatCommand> commands) {
    this.event = event;
    this.history = history;
    this.commands = commands;
    execute();
    this.event = null;
    this.history = null;
    this.commands = null;
  }

  public abstract void execute();

  protected ServerChatEvent event() {
    return event;
  }

  protected PlayerHistory history() {
    return history;
  }

  protected List<ChatCommand> commands() {
    return commands;
  }

  protected EntityPlayerMP player() {
    return event.getPlayer();
  }

  protected WorldServer world() {
    return player().getServerWorld();
  }

  protected void print(String text) {
    player().sendMessage(new TextComponentString(TextFormatting.GREEN + text));
  }

  @Nullable
  protected Area area() {
    if (!markedPosHasAtLeast(2)) {
      print("Error: need two marked pos. Place more torch.");
      return null;
    }
    return new Area(markedPos(0), markedPos(1));
  }

  @Nullable
  protected BlockPos pos() {
    if (!markedPosHasAtLeast(1)) {
      print("Error: need one marked pos. Place more torch.");
      return null;
    }
    return markedPos(0);
  }

  protected boolean markedPosHasAtLeast(int count) {
    return history.markedPos.size() >= count;
  }

  protected BlockPos markedPos(int index) {
    return history.markedPos.get(history.markedPos.size() - 1 - index);
  }

  protected BlockChangeList addBlockChange() {
    BlockChangeList change = new BlockChangeList(world());
    history.addChange(change);
    return change;
  }

  @Nullable
  protected IBlockState mainHandHeldBlockState() {
    return heldBlockState(mainHandHeldStack());
  }

  @Nullable
  protected IBlockState offHandHeldBlockState() {
    return heldBlockState(offHandHeldStack());
  }

  @Nullable
  private IBlockState heldBlockState( ItemStack stack) {
    if (stack.getItem() instanceof ItemBlock) {
      Block block = ((ItemBlock) stack.getItem()).getBlock();
      if (block.equals(Blocks.TORCH)) {
        print("Error: not block in your hand, only a torch.");
        return null;
      }
      return block.getStateFromMeta(stack.getItem().getMetadata(stack));
    }
    print("Error: not block in your hand.");
    return null;
  }

  protected ItemStack mainHandHeldStack() {
    return player().getHeldItemMainhand();
  }

  protected ItemStack offHandHeldStack() {
    return player().getHeldItemOffhand();
  }

  protected String msg() {
    return event.getMessage();
  }

  protected String arg(int pos) {
    String[] args = msg().split(" ");
    if (pos < args.length) {
      return args[pos];
    } else {
      return null;
    }
  }

  protected Integer integer(String value) {
    if (value == null) {
      print("Error: missing integer value.");
      return null;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      print("Error: invalid integer value: " + value);
      return null;
    }
  }

  protected String argAsString(int pos) {
    String value = arg(pos);
    if (value == null) {
      print("Error: missing string value.");
    }
    return value;
  }

  protected Vec3i multiply(Vec3i direction, int value) {
    return new Vec3i(direction.getX() * value, direction.getY() * value, direction.getZ() * value);
  }

  protected Vec3i direction(String value) {
    if (value == null) {
      print("Error: missing direction, use: x OR y OR z");
      return null;
    }
    switch (value.toLowerCase(ROOT)) {
      case "x":
      case "+x":
        return new Vec3i(1, 0, 0);
      case "-x":
        return new Vec3i(-1, 0, 0);
      case "y":
      case "+y":
        return new Vec3i(0, 1, 0);
      case "-y":
        return new Vec3i(0, -1, 0);
      case "z":
      case "+z":
        return new Vec3i(0, 0, 1);
      case "-z":
        return new Vec3i(0, 0, -1);
      default:
        print("Error: invalid direction, use: x OR y OR z");
        return null;
    }
  }

  protected EnumFacing.Axis axisArgument(int argumentIndex) {
    String value = arg(argumentIndex);
    if (value == null) {
      print("Error: missing axis, use: x OR y OR z");
      return null;
    }
    switch (value.toLowerCase(ROOT)) {
      case "x":
        return EnumFacing.Axis.X;
      case "y":
        return EnumFacing.Axis.Y;
      case "z":
        return EnumFacing.Axis.Z;
      default:
        print("Error: invalid axis, use: x OR y OR z");
        return null;
    }
  }

}
