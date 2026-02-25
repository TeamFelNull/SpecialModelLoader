package dev.felnull.specialmodelloader.impl.handler;

import com.google.common.collect.ImmutableMap;
import dev.felnull.specialmodelloader.api.SpecialModelLoaderAPI;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import dev.felnull.specialmodelloader.api.model.LoadedResource;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public final class SMLModelLoadingHandler {

    public static void init() {
        PreparableModelLoadingPlugin.register(SMLModelLoadingHandler::loadPrepareData, new MyModelLoadingPlugin());
    }

    private static UnbakedModel modifyModelOnLoad(PreparationData data, UnbakedModel original,
            ModelModifier.OnLoad.Context context) {

        Identifier resId = context.id();
        if (resId != null) {
            LoadedResource loadedResource = data.resources().get(resId);
            if (loadedResource != null) {
                return SpecialModelLoaderAPI.getInstance().makeModel(loadedResource);
            }
        }

        return original;
    }

    private static CompletableFuture<PreparationData> loadPrepareData(PreparableReloadListener.SharedState sharedState,
            Executor executor) {
        ResourceManager resourceManager = sharedState.resourceManager();
        CompletableFuture<Predicate<Identifier>> loadScopePredicates = SpecialModelLoaderEvents.LOAD_SCOPE_ASYNC
                .invoker().provideAsyncLoadScopePredicate(resourceManager, executor);
        return loadScopePredicates.thenApplyAsync((lpPre) -> {
            ImmutableMap.Builder<Identifier, LoadedResource> resBuilder = ImmutableMap.builder();

            resourceManager.listResources("models", loc -> loc.getPath().endsWith(".json"))
                    .forEach((location, resource) -> {
                        String path = location.getPath().substring("models/".length());
                        path = path.substring(0, path.length() - ".json".length());
                        Identifier modelLoc = Identifier.fromNamespaceAndPath(location.getNamespace(), path);

                        if (!lpPre.test(modelLoc)) {
                            return;
                        }

                        LoadedResource res = SpecialModelLoaderAPI.getInstance().loadResource(resourceManager,
                                modelLoc);
                        if (res != null) {
                            resBuilder.put(modelLoc, res);
                        }
                    });

            return new PreparationData(resBuilder.build());
        }, executor);
    }

    private static class MyModelLoadingPlugin implements PreparableModelLoadingPlugin<PreparationData> {
        @Override
        public void initialize(PreparationData data, ModelLoadingPlugin.Context pluginContext) {
            pluginContext.modifyModelOnLoad().register((model, context) -> modifyModelOnLoad(data, model, context));
        }
    }

    private record PreparationData(Map<Identifier, LoadedResource> resources) {
    }
}
