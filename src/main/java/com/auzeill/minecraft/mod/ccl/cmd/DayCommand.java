package com.auzeill.minecraft.mod.ccl.cmd;

public class DayCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".day";
  }

  @Override
  public String getDescription() {
    return "Change time to day time. ex: .day";
  }

  @Override
  public void execute() {
    world().setWorldTime(1000);
    print("Set day time.");
  }

}
