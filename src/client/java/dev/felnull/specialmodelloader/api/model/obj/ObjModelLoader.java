package dev.felnull.specialmodelloader.api.model.obj;

import dev.felnull.specialmodelloader.api.model.LoadedResource;
import dev.felnull.specialmodelloader.api.model.ModelLoader;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjModelLoader extends ModelLoader {

    /**
     * Load resource from OBJ file location and options.
     *
     * @param resourceManager ResourceManager
     * @param location        OBJ Location
     * @param option          Model Option
     * @return Loaded Model, null if not loaded.
     */
    @Nullable
    LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ObjModelOption option);

    /**
     * Load model from OBJ file location and options.
     *
     * @param resourceManager ResourceManager
     * @param location        OBJ Location
     * @param option          Model Option
     * @return UnbakedModel
     */
    default @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ObjModelOption option) {
        LoadedResource res = loadResource(resourceManager, location, option);

        if (res == null) {
            return null;
        }

        return makeModel(res);
    }
}
