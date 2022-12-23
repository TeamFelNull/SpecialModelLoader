package dev.felnull.specialmodelloader.impl.handler;

import dev.felnull.specialmodelloader.api.SpecialModelLoaderAPI;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;

public class SMLModelResourceHandler implements ModelResourceProvider {
    private final ResourceManager resourceManager;

    public SMLModelResourceHandler(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public static void init() {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(SMLModelResourceHandler::new);
        SpecialModelLoaderEvents.LOAD_SCOPE.register(SMLModelResourceHandler::isLoadScope);
    }

    @Override
    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
        if (SpecialModelLoaderEvents.LOAD_SCOPE.invoker().isLoadScope(resourceId))
            return SpecialModelLoaderAPI.getInstance().loadModel(resourceManager, resourceId);
        return null;
    }

    private static boolean isLoadScope(ResourceLocation location) {
        return SpecialModelLoader.MODID.equals(location.getNamespace()) || "sml".equals(location.getNamespace());
    }
}
