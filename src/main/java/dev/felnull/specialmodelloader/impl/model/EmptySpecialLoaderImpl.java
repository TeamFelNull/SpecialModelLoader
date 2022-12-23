package dev.felnull.specialmodelloader.impl.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.EmptyModelLoader;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.impl.util.JsonModelUtils;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptySpecialLoaderImpl implements EmptyModelLoader {
    @Override
    public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull JsonObject modelJson) throws ModelProviderException {
        return new EmptySpecialUnbakedModel(ModelOption.parse(modelJson), JsonModelUtils.getParentLocation(modelJson));
    }

    @Override
    public @Nullable String getId() {
        return "empty";
    }

    @Override
    public boolean isUse(@NotNull ResourceLocation modelLocation) {
        return false;
    }
}
