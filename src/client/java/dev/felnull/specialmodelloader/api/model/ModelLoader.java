package dev.felnull.specialmodelloader.api.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModelLoader {

    /**
     * Load the resource needed to create the model from the Resource Manager and Model Json.
     *
     * @param resourceManager Resource Manager
     * @param modelJson       Model json
     * @return Loaded Model, null if not loaded.
     */
    @Nullable LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull JsonObject modelJson);

    /**
     * Make model from loaded resources.
     *
     * @param loadedResource Loaded resource
     * @return UnbakedModel
     */
    @NotNull UnbakedModel makeModel(@NotNull LoadedResource loadedResource);

    /**
     * Load model from Resource Manager and Model Json.
     *
     * @param resourceManager Resource Manager
     * @param modelJson       Model json
     * @return UnbakedModel
     */
    default @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull JsonObject modelJson) {
        LoadedResource res = loadResource(resourceManager, modelJson);

        if (res == null) {
            return null;
        }

        return makeModel(res);
    }

    /**
     * ID of this model loader
     *
     * @return ID
     */
    @Nullable String getId();

    /**
     * Whether the model uses this model loader or not.
     *
     * @param modelLocation Model Location
     * @return true if this loader is used, false if not.
     */
    default boolean isLoaderLocation(@NotNull ResourceLocation modelLocation) {
        if (SpecialModelLoader.MODID.equals(modelLocation.getNamespace()) || "sml".equals(modelLocation.getNamespace()))
            return modelLocation.getPath().equals("builtin/" + getId());
        return false;
    }
}
