package com.auzeill.minecraft.mod.ccl;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
  modid = "creation_command_line",
  name = "Creation Command Line",
  version = "1.0")
public class CreationCommandLineMod {

  @EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new CreationCommandLineEvents());
  }

}

