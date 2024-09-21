package dev.felnull.specialmodelloader.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class SpecialModelDataGenHelper {
    private static final ResourceLocation OBJ_LOADER = ResourceLocation.fromNamespaceAndPath(SpecialModelLoader.MODID, "builtin/obj");

    public static void generateObjModel(@NotNull ResourceLocation location, @NotNull ResourceLocation objLocation,
                                        boolean flipV, boolean useAmbientOcclusion, @Nullable String mtlOverride, @Unmodifiable @NotNull Map<String, ResourceLocation> textures,
                                        @Nullable ResourceLocation particle, @NotNull BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {

        output.accept(location, () -> {
            var jo = new JsonObject();
            jo.addProperty("parent", OBJ_LOADER.toString());
            jo.addProperty("model", objLocation.toString());
            jo.addProperty("flip_v", flipV);
            jo.addProperty("ambientocclusion", useAmbientOcclusion);

            if (mtlOverride != null) {
                jo.addProperty("mtl_override", mtlOverride);
            }

            if (!textures.isEmpty()) {
                JsonObject texturesJo = new JsonObject();
                textures.forEach((name, loc) -> {
                    texturesJo.addProperty(name, loc.toString());
                });
                jo.add("textures", texturesJo);
            }

            if (particle != null) {
                jo.addProperty("particle", particle.toString());
            }

            return jo;
        });
    }

    public static void generateObjModel(@NotNull Item item, @NotNull ResourceLocation objLocation,
                                        boolean flipV, boolean useAmbientOcclusion, @Nullable String mtlOverride, @Unmodifiable @NotNull Map<String, ResourceLocation> textures,
                                        @Nullable ResourceLocation particle, @NotNull BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
        generateObjModel(ModelLocationUtils.getModelLocation(item), objLocation, flipV, useAmbientOcclusion, mtlOverride, textures, particle, output);
    }

    public static void generateObjModel(@NotNull Block block, @NotNull ResourceLocation objLocation,
                                        boolean flipV, boolean useAmbientOcclusion, @Nullable String mtlOverride, @Unmodifiable @NotNull Map<String, ResourceLocation> textures,
                                        @Nullable ResourceLocation particle, @NotNull BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
        generateObjModel(ModelLocationUtils.getModelLocation(block), objLocation, flipV, useAmbientOcclusion, mtlOverride, textures, particle, output);
    }
}
