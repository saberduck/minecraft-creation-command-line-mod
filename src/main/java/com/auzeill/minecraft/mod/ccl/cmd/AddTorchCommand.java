package com.auzeill.minecraft.mod.ccl.cmd;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class AddTorchCommand extends ChatCommand {

  @Override
  public String getName() {
    return ".torch";
  }

  @Override
  public String getDescription() {
    return "Add 64 torches to the player inventory. ex: .torch";
  }

  @Override
  public void execute() {
    player().inventory.addItemStackToInventory(new ItemStack(Blocks.TORCH, 64));
    print("Added 64 torches.");
  }

}
