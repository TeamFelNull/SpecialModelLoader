package dev.felnull.specialmodelloader.api;

import dev.felnull.specialmodelloader.impl.model.ObjLoaderImp;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjLoader {
    static ObjLoader getInstance() {
        return ObjLoaderImp.INSTANCE;
    }

    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location) throws ModelProviderException;

    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ItemTransforms transforms, @NotNull ObjOption option) throws ModelProviderException;
}
