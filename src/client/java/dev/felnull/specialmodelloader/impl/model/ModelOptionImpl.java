package dev.felnull.specialmodelloader.impl.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.impl.mixin.BlockModelAccessor;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ModelOptionImpl(boolean useAmbientOcclusion, UnbakedModel.GuiLight guiLight, Identifier particle,
        ItemTransforms transforms) implements ModelOption {

    public static ModelOptionImpl parse(JsonObject modelJson) {
        ItemTransforms transform = ItemTransforms.NO_TRANSFORMS;

        if (modelJson.has("display")) {
            var jo = GsonHelper.getAsJsonObject(modelJson, "display");
            transform = BlockModelAccessor.getGson().fromJson(jo, ItemTransforms.class);
        }

        UnbakedModel.GuiLight guiLight = null;
        if (modelJson.has("gui_light"))
            guiLight = UnbakedModel.GuiLight.getByName(GsonHelper.getAsString(modelJson, "gui_light"));

        Identifier particle = null;
        if (modelJson.has("particle"))
            particle = Identifier.parse(GsonHelper.getAsString(modelJson, "particle"));

        return new ModelOptionImpl(GsonHelper.getAsBoolean(modelJson, "ambientocclusion", true), guiLight, particle,
                transform);
    }

    @Override
    public boolean isUseAmbientOcclusion() {
        return useAmbientOcclusion;
    }

    @Override
    public @Nullable UnbakedModel.GuiLight getGuiLight() {
        return guiLight;
    }

    @Override
    public @Nullable Identifier getParticle() {
        return particle;
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return transforms;
    }
}
