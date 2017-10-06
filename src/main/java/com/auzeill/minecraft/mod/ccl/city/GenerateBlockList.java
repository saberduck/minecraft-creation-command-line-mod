package com.auzeill.minecraft.mod.ccl.city;

import com.auzeill.minecraft.mod.ccl.world.Serializer;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

public class GenerateBlockList {

  public static void main(String[] args) throws IOException {
    Bootstrap.register();

    File sourceFile = new File("src/main/java/com/auzeill/minecraft/mod/ccl/city/Block.java");
    try (BufferedWriter code = Files.newWriter(sourceFile, UTF_8)) {
      code.append("package com.auzeill.minecraft.mod.ccl.city;\n");
      code.append("\n");
      code.append("public interface Block {\n");
      code.append("\n");
      for (Block block : Block.REGISTRY) {
        ImmutableList<IBlockState> states = block.getBlockState().getValidStates();
        if (states.size() == 1) {
          code.append("  String " + blockName(block) +  " = \""+ stateValue(states.get(0)) +"\";\n");
        } else {
          code.append("  interface " + blockName(block) +  " {\n");
          for (IBlockState state : states) {
            code.append("    String " + stateName(state) +  " = \""+ stateValue(state) +"\";\n");
          }
          code.append("  }\n");
        }
      }
      code.append("\n");
      code.append("}\n");
    }
  }

  private static String blockName(Block block) {
    ResourceLocation name = block.getRegistryName();
    return WordUtils.capitalizeFully(name.getResourcePath(), '_', '-', '.').replaceAll("[_\\-.]","");
  }

  private static String stateName(IBlockState state) {
    return WordUtils.capitalizeFully(state.toString()
      .replaceAll("variant=","")
      .replaceAll("type=","")
      .replaceAll("facing=","")
      .replaceAll("color=","")
      .replaceAll("mode=","")
      .replaceAll("^[^\\[]*\\[","")
      .replaceAll("[,=]"," ")
      .replaceAll("]$",""), ' ', '_', '-', '.').replaceAll("[ _\\-.]","");
  }

  private static String stateValue(IBlockState state) {
    return Serializer.blockStateToString(state);
  }

}
