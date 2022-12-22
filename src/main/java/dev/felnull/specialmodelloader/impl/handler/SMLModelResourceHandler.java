package dev.felnull.specialmodelloader.impl.handler;

import dev.felnull.specialmodelloader.api.ObjLoader;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class SMLModelResourceHandler implements ModelResourceProvider {

    private final ResourceManager resourceManager;

    public SMLModelResourceHandler(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public static void init() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(SMLModelResourceHandler::new);
    }

    @Override
    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
        return ObjLoader.getInstance().loadModel(resourceManager, resourceId);
    }
}
