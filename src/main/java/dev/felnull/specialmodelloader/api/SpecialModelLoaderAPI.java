package dev.felnull.specialmodelloader.api;

import dev.felnull.specialmodelloader.api.model.EmptyModelLoader;
import dev.felnull.specialmodelloader.api.model.SpecialBaseLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.impl.model.SpecialModelLoaderAPIImpl;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
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

    @Unmodifiable @NotNull List<SpecialBaseLoader> getUseLoaders();

    @NotNull ObjModelLoader getObjLoader();

    @NotNull EmptyModelLoader getEmptyLoader();

    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation) throws ModelProviderException;
}
