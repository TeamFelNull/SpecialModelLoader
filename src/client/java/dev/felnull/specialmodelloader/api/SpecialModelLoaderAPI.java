package dev.felnull.specialmodelloader.api;

import dev.felnull.specialmodelloader.api.model.LoadedResource;
import dev.felnull.specialmodelloader.api.model.ModelLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.impl.SpecialModelLoaderAPIImpl;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface SpecialModelLoaderAPI {

    static SpecialModelLoaderAPI getInstance() {
        return SpecialModelLoaderAPIImpl.INSTANCE;
    }

    /**
     * A complete list of model loaders supported by this mod.<br/>
     * Currently, there is only an OBJ loader
     *
     * @return ModelLoader list
     */
    @Unmodifiable
    @NotNull List<ModelLoader> getLoaders();

    /**
     * A loader for reading OBJ files
     *
     * @return OBJModel Loader
     */
    @NotNull ObjModelLoader getObjLoader();

    /**
     * Loads a model at a specified location using the resource manager and returns the loaded resource.
     *
     * @param resourceManager ResourceManager
     * @param modelLocation   The location of the JsonModel (e.g. minecraft:item/apple)
     * @return The loaded resource instances required to make the model. Null if unable to load.
     */
    @Nullable
    LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation);

    /**
     * Made a model from the loaded resources.
     *
     * @param resource Resources loaded using{@link #loadResource(ResourceManager, ResourceLocation)}
     * @return Made model
     */
    @NotNull
    UnbakedModel makeModel(@NotNull LoadedResource resource);
}
