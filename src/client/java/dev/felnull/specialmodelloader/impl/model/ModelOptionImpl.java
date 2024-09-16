package dev.felnull.specialmodelloader.impl.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.impl.mixin.BlockModelAccessor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ModelOptionImpl(boolean useAmbientOcclusion, BlockModel.GuiLight guiLight, ResourceLocation particle,
                              ItemTransforms transforms) implements ModelOption {

    public static ModelOptionImpl parse(JsonObject modelJson) {
        ItemTransforms transform = ItemTransforms.NO_TRANSFORMS;

        if (modelJson.has("display")) {
            var jo = GsonHelper.getAsJsonObject(modelJson, "display");
            transform = BlockModelAccessor.getGson().fromJson(jo, ItemTransforms.class);
        }

        BlockModel.GuiLight guiLight = null;
        if (modelJson.has("gui_light"))
            guiLight = BlockModel.GuiLight.getByName(GsonHelper.getAsString(modelJson, "gui_light"));

        ResourceLocation particle = null;
        if (modelJson.has("particle"))
            particle = ResourceLocation.parse(GsonHelper.getAsString(modelJson, "particle"));

        return new ModelOptionImpl(GsonHelper.getAsBoolean(modelJson, "ambientocclusion", true), guiLight, particle, transform);
    }

    @Override
    public boolean isUseAmbientOcclusion() {
        return useAmbientOcclusion;
    }

    @Override
    public @Nullable BlockModel.GuiLight getGuiLight() {
        return guiLight;
    }

    @Override
    public @Nullable ResourceLocation getParticle() {
        return particle;
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return transforms;
    }
}
