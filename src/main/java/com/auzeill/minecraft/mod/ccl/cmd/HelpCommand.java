package com.auzeill.minecraft.mod.ccl.cmd;

public class HelpCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".help";
  }

  @Override
  public String getDescription() {
    return "Display this help. ex: .help";
  }

  @Override
  public void execute() {
    print("To select an area place two torches, or try to break blocks with a torch.");
    print("Command List:");
    commands().forEach(command -> print("  " + command.getName() + " : " + command.getDescription()));
  }

}
