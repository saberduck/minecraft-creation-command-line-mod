package com.auzeill.minecraft.mod.ccl;

import com.auzeill.minecraft.mod.ccl.cmd.AddTorchCommand;
import com.auzeill.minecraft.mod.ccl.cmd.BoxCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ChatCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ClearCommand;
import com.auzeill.minecraft.mod.ccl.cmd.CopyCommand;
import com.auzeill.minecraft.mod.ccl.cmd.DayCommand;
import com.auzeill.minecraft.mod.ccl.cmd.DeleteCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ExpandCommand;
import com.auzeill.minecraft.mod.ccl.cmd.FillCommand;
import com.auzeill.minecraft.mod.ccl.cmd.HelpCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ListCommand;
import com.auzeill.minecraft.mod.ccl.cmd.LoadCommand;
import com.auzeill.minecraft.mod.ccl.cmd.MergeCommand;
import com.auzeill.minecraft.mod.ccl.cmd.MirrorCommand;
import com.auzeill.minecraft.mod.ccl.cmd.NoWaterCommand;
import com.auzeill.minecraft.mod.ccl.cmd.PasteCommand;
import com.auzeill.minecraft.mod.ccl.cmd.RectangleCommand;
import com.auzeill.minecraft.mod.ccl.cmd.RedoCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ReplaceCommand;
import com.auzeill.minecraft.mod.ccl.cmd.RotateCommand;
import com.auzeill.minecraft.mod.ccl.cmd.SaveCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ShiftCommand;
import com.auzeill.minecraft.mod.ccl.cmd.ShrinkCommand;
import com.auzeill.minecraft.mod.ccl.cmd.UndoCommand;
import com.auzeill.minecraft.mod.ccl.cmd.WallCommand;
import com.auzeill.minecraft.mod.ccl.world.BlockChange;
import com.auzeill.minecraft.mod.ccl.world.BlockChangeList;
import com.auzeill.minecraft.mod.ccl.world.PlayerHistory;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

class CreationCommandLineEvents {

  private PlayerHistory history = new PlayerHistory();

  private final List<ChatCommand> commands = Arrays.asList(
    new AddTorchCommand(),
    new BoxCommand(),
    new ClearCommand(),
    new CopyCommand(),
    new DayCommand(),
    new DeleteCommand(),
    new ExpandCommand(),
    new FillCommand(),
    new HelpCommand(),
    new ListCommand(),
    new LoadCommand(),
    new MergeCommand(),
    new MirrorCommand(),
    new NoWaterCommand(),
    new PasteCommand(),
    new RedoCommand(),
    new RectangleCommand(),
    new ReplaceCommand(),
    new RotateCommand(),
    new SaveCommand(),
    new ShiftCommand(),
    new ShrinkCommand(),
    new UndoCommand(),
    new WallCommand());

  @SubscribeEvent
  public void breakEvent(BlockEvent.BreakEvent event) {
    Item heldItem = event.getPlayer().getHeldItemMainhand().getItem();
    if (heldItem instanceof ItemBlock) {
      ItemBlock itemBlock = (ItemBlock) heldItem;
      if (itemBlock.getBlock().equals(Blocks.TORCH)) {
        event.setCanceled(true);
        history.addPos(event.getPlayer(), event.getPos());
      } else {
        BlockChangeList change = new BlockChangeList(event.getWorld());
        change.changes.add(new BlockChange(event.getPos(), event.getState(), Blocks.AIR.getDefaultState()));
        history.addChange(change);
      }
    }
  }

  @SubscribeEvent
  public void placeEvent(BlockEvent.PlaceEvent event) {
    if (event.getPlacedBlock().getBlock().equals(Blocks.TORCH)) {
      history.addPos(event.getPlayer(), event.getPos());
    }
    BlockChangeList change = new BlockChangeList(event.getWorld());
    if (event instanceof BlockEvent.MultiPlaceEvent) {
      for (BlockSnapshot snapshot : ((BlockEvent.MultiPlaceEvent)event).getReplacedBlockSnapshots()) {
        addSnapshot(change, snapshot);
      }
    } else {
      addSnapshot(change, event.getBlockSnapshot());
    }
    history.addChange(change);
  }

  private static void addSnapshot(BlockChangeList change, BlockSnapshot snapshot) {
    IBlockState replacedBlock = snapshot.getReplacedBlock();
    if (replacedBlock == null) {
      replacedBlock = Blocks.AIR.getDefaultState();
    }
    change.changes.add(new BlockChange(snapshot.getPos(), replacedBlock, snapshot.getCurrentBlock()));
  }

  @SubscribeEvent
  public void chatEvent(ServerChatEvent event) {
    String message = event.getMessage();
    if (message.startsWith(".")) {
      boolean validCommand = false;
      for (ChatCommand command : commands) {
        if (command.accept(message)) {
          command.execute(event, history, commands);
          validCommand = true;
          break;
        }
      }
      if (!validCommand) {
        event.getPlayer().sendMessage(new TextComponentString(TextFormatting.GREEN + "ERROR, Unknown command: " + message));
      }
    }
  }

}
