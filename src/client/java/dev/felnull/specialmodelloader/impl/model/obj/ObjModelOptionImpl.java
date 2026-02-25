package dev.felnull.specialmodelloader.impl.model.obj;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import dev.felnull.specialmodelloader.impl.util.JsonUtils;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public record ObjModelOptionImpl(ModelOption modelOption, boolean flipV, String mtlOverride,
        Map<String, Identifier> textures) implements ObjModelOption {

    public static ObjModelOptionImpl parse(JsonObject modelJson) {
        boolean flipV = GsonHelper.getAsBoolean(modelJson, "flip_v", false);
        String mtlOverride = GsonHelper.getAsString(modelJson, "mtl_override", null);

        ImmutableMap.Builder<String, Identifier> textures = ImmutableMap.builder();
        if (modelJson.has("textures") && modelJson.get("textures").isJsonObject()) {
            JsonObject texturesJo = modelJson.getAsJsonObject("textures");
            texturesJo.keySet().forEach(key -> {
                Identifier tex = JsonUtils.getResourceLocation(texturesJo, key);
                if (tex != null) {
                    textures.put(key, tex);
                }
            });
        }

        return new ObjModelOptionImpl(ModelOption.parse(modelJson), flipV, mtlOverride, textures.build());
    }

    @Override
    public boolean isFlipV() {
        return flipV;
    }

    @Nullable
    @Override
    public String getMtlOverride() {
        return mtlOverride;
    }

    @Override
    public @Unmodifiable @NotNull Map<String, Identifier> getTextures() {
        return textures;
    }

    @Override
    public boolean isUseAmbientOcclusion() {
        return modelOption.isUseAmbientOcclusion();
    }

    @Override
    public @Nullable UnbakedModel.GuiLight getGuiLight() {
        return modelOption.getGuiLight();
    }

    @Override
    public @Nullable Identifier getParticle() {
        return modelOption.getParticle();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return modelOption.getTransforms();
    }
}
