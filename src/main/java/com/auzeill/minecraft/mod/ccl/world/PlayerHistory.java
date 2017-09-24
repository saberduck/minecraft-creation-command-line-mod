package com.auzeill.minecraft.mod.ccl.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class PlayerHistory {

  public List<Change> changeHistory = new ArrayList<>();
  public int changeHistoryPos = 0;

  public final List<BlockPos> markedPos = new ArrayList<>();

  public CopiedArea copiedArea = null;

  public void addChange(Change change) {
    for (int i = changeHistory.size() - 1; i >= changeHistoryPos; i--) {
      changeHistory.remove(i);
    }
    changeHistory.add(change);
    changeHistoryPos++;
  }

  public void addPos(EntityPlayer player, BlockPos pos) {
    while (markedPos.size() > 99) {
      markedPos.remove(0);
    }
    markedPos.add(pos);
    player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Save " + pos));
  }

}
