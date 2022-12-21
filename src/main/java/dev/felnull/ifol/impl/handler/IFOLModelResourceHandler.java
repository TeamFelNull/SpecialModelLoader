package dev.felnull.ifol.impl.handler;

import dev.felnull.ifol.api.OBJLoader;
import net.fabricmc.fabric.api.client.model.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class IFOLModelResourceHandler implements ModelResourceProvider, ModelVariantProvider {

    private final ResourceManager resourceManager;

    public IFOLModelResourceHandler(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public static void init() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(IFOLModelResourceHandler::new);
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(IFOLModelResourceHandler::new);
    }

    @Override
    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
        return OBJLoader.getInstance().loadModel(resourceManager, resourceId);
    }

    @Override
    public @Nullable UnbakedModel loadModelVariant(ModelResourceLocation modelId, ModelProviderContext context) throws ModelProviderException {
        return null;
    }
}
