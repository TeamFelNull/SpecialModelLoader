package dev.felnull.specialmodelloader.api.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.impl.model.ModelOptionImpl;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModelOption {

    @NotNull
    static ModelOption of(boolean useAmbientOcclusion, @Nullable UnbakedModel.GuiLight guiLight,
            @Nullable Identifier particle, @NotNull ItemTransforms transforms) {
        return new ModelOptionImpl(useAmbientOcclusion, guiLight, particle, transforms);
    }

    @NotNull
    static ModelOption parse(@NotNull JsonObject modelJson) {
        return ModelOptionImpl.parse(modelJson);
    }

    boolean isUseAmbientOcclusion();

    @Nullable
    UnbakedModel.GuiLight getGuiLight();

    @Nullable
    Identifier getParticle();

    @NotNull
    ItemTransforms getTransforms();
}
