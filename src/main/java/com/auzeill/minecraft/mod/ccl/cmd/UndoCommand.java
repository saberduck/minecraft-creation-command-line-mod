package com.auzeill.minecraft.mod.ccl.cmd;

public class UndoCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".u";
  }

  @Override
  public String getDescription() {
    return "Undo the last block command. ex: .u";
  }

  @Override
  public void execute() {
    if (history().changeHistoryPos > 0) {
      history().changeHistoryPos--;
      history().changeHistory.get(history().changeHistoryPos).undo();
      print("Undo success.");
    } else {
      print("ERROR: nothing to undo.");
    }
  }

}
