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

    @Unmodifiable
    @NotNull List<ModelLoader> getLoaders();

    @NotNull ObjModelLoader getObjLoader();

    @Nullable
    LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation);

    @NotNull
    UnbakedModel makeModel(@NotNull LoadedResource resource);
}
