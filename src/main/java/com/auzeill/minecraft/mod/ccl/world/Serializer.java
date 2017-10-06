package com.auzeill.minecraft.mod.ccl.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Serializer {

  private Serializer() {
  }

  public static final Gson GSON = new GsonBuilder()
    .registerTypeHierarchyAdapter(IBlockState.class, new IBlockStateAdapter())
    .setPrettyPrinting()
    .create();

  public static void serializeCopiedArea(Path path, CopiedArea copiedArea) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path, UTF_8)) {
      GSON.toJson(copiedArea, writer);
    }
  }

  public static CopiedArea deserializeCopiedArea(Path path) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(path, UTF_8)) {
      return GSON.fromJson(reader, CopiedArea.class);
    }
  }

  public static class IBlockStateAdapter implements JsonSerializer<IBlockState>, JsonDeserializer<IBlockState> {
    @Override
    public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(blockStateToString(src));
    }

    @Override
    public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      String[] blockState = json.getAsJsonPrimitive().getAsString().split(":", -1);
      if (blockState.length != 3) {
        throw new JsonParseException("IBlockState deserialize(), invalid value: " + json.toString());
      }
      String domain = blockState[0].isEmpty() ? "minecraft" : blockState[0];
      String blockName = blockState[1];
      ResourceLocation nameForObject = new ResourceLocation(domain, blockName);
      Block block = Block.REGISTRY.getObject(nameForObject);
      int meta = Integer.parseInt(blockState[2]);
      return block.getStateFromMeta(meta);
    }
  }

  public static String blockStateToString(IBlockState state) {
    ResourceLocation registryName = state.getBlock().getRegistryName();
    if (registryName == null) {
      throw new IllegalStateException();
    }
    String domain = registryName.getResourceDomain();
    if (domain.equals("minecraft")) {
      domain = "";
    }
    int meta = state.getBlock().getMetaFromState(state);
    return domain + ":" + registryName.getResourcePath() + ":" + meta;
  }

}
