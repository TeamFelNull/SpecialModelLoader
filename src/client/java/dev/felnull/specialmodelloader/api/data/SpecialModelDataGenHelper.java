package dev.felnull.specialmodelloader.api.data;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.function.BiConsumer;

public final class SpecialModelDataGenHelper {
    private static final Identifier OBJ_LOADER = Identifier.fromNamespaceAndPath(SpecialModelLoader.MODID,
            "builtin/obj");

    public static void generateObjModel(@NotNull Identifier location, @NotNull Identifier objLocation,
            boolean flipV, boolean useAmbientOcclusion, @Nullable String mtlOverride,
            @Unmodifiable @NotNull Map<String, Identifier> textures,
            @Nullable Identifier particle, @NotNull BiConsumer<Identifier, ModelInstance> output) {

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

    public static void generateObjModel(@NotNull Item item, @NotNull Identifier objLocation,
            boolean flipV, boolean useAmbientOcclusion, @Nullable String mtlOverride,
            @Unmodifiable @NotNull Map<String, Identifier> textures,
            @Nullable Identifier particle, @NotNull BiConsumer<Identifier, ModelInstance> output) {
        generateObjModel(ModelLocationUtils.getModelLocation(item), objLocation, flipV, useAmbientOcclusion,
                mtlOverride, textures, particle, output);
    }

    public static void generateObjModel(@NotNull Block block, @NotNull Identifier objLocation,
            boolean flipV, boolean useAmbientOcclusion, @Nullable String mtlOverride,
            @Unmodifiable @NotNull Map<String, Identifier> textures,
            @Nullable Identifier particle, @NotNull BiConsumer<Identifier, ModelInstance> output) {
        generateObjModel(ModelLocationUtils.getModelLocation(block), objLocation, flipV, useAmbientOcclusion,
                mtlOverride, textures, particle, output);
    }
}
