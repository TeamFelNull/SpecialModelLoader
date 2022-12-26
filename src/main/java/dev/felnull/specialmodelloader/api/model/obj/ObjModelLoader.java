package dev.felnull.specialmodelloader.api.model.obj;

import dev.felnull.specialmodelloader.api.model.SpecialBaseLoader;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjModelLoader extends SpecialBaseLoader {
    @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ObjModelOption option) throws ModelProviderException;
}
