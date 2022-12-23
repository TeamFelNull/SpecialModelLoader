package dev.felnull.specialmodelloader.api.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SpecialBaseLoader {
    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull JsonObject modelJson) throws ModelProviderException;

    @Nullable String getId();

    default boolean isUse(@NotNull ResourceLocation modelLocation) {
        if (SpecialModelLoader.MODID.equals(modelLocation.getNamespace()) || "sml".equals(modelLocation.getNamespace()))
            return modelLocation.getPath().equals("builtin/" + getId());
        return false;
    }
}
