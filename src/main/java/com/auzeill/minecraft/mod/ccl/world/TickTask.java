package com.auzeill.minecraft.mod.ccl.world;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;

public class TickTask {

  private static final Set<TickTask> ALL_TASK = ConcurrentHashMap.newKeySet();

  private final long sleepTime;
  private final BooleanSupplier task;
  private long lastExecTime = 0;
  private boolean stop = false;

  private TickTask(long sleepTime, BooleanSupplier task) {
    this.sleepTime = sleepTime;
    this.task = task;
  }

  public void stop() {
    stop = true;
  }

  private boolean tick() {
    if (stop) {
      return false;
    }
    long now = System.currentTimeMillis();
    if (now - lastExecTime > sleepTime) {
      lastExecTime = now;
      return task.getAsBoolean();
    }
    return true;
  }

  public static TickTask create(long sleepTime, BooleanSupplier task) {
    TickTask tickTask = new TickTask(sleepTime, task);
    ALL_TASK.add(tickTask);
    return tickTask;
  }

  public static void tickAll() {
    if (ALL_TASK.isEmpty()) {
      return;
    }
    ALL_TASK.removeIf(tickTask -> !tickTask.tick());
  }

}
