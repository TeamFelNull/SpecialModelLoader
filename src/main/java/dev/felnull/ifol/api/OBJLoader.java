package dev.felnull.ifol.api;

import dev.felnull.ifol.impl.model.OBJLoaderImp;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OBJLoader {
    static OBJLoader getInstance() {
        return OBJLoaderImp.INSTANCE;
    }

    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location) throws ModelProviderException;

    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ItemTransforms transforms, @NotNull OBJOption option) throws ModelProviderException;
}
