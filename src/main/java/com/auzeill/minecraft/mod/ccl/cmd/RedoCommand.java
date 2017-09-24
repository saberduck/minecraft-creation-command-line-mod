package com.auzeill.minecraft.mod.ccl.cmd;

public class RedoCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".r";
  }

  @Override
  public String getDescription() {
    return "Redo the last undone block command. ex: .r";
  }

  @Override
  public void execute() {
    if (history().changeHistoryPos < history().changeHistory.size()) {
      history().changeHistory.get(history().changeHistoryPos).execute();
      history().changeHistoryPos++;
      print("Redo success.");
    } else {
      print("ERROR: nothing to redo.");
    }
  }

}
