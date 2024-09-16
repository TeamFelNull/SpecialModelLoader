package dev.felnull.specialmodelloader.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class SpecialModelLoaderEvents {

    public static final Event<LoadScope> LOAD_SCOPE = EventFactory.createArrayBacked(LoadScope.class, loadScopes -> {
        BiPredicate<ResourceManager, ResourceLocation> predicate = Arrays.stream(loadScopes)
                .map(LoadScope::provideLoadScopePredicate)
                .reduce(BiPredicate::or)
                .orElseGet(() -> (res, loc) -> false);
        return () -> predicate;
    });

    public static final Event<AsyncLoadScope> LOAD_SCOPE_ASYNC = EventFactory.createArrayBacked(AsyncLoadScope.class, loadScopes -> (resourceManager, executor) ->
            Arrays.stream(loadScopes)
                    .map(it -> it.provideAsyncLoadScopePredicate(resourceManager, executor))
                    .reduce((cf1, cf2) -> cf1.thenCombineAsync(cf2, Predicate::or, executor))
                    .orElseGet(() -> CompletableFuture.completedFuture(loc -> false)));

    public interface LoadScope {

        /**
         * It must return a Predicate to check if the given resource location is Load Scope.<br/>
         * This method is called from the rendering thread, but the Predicate test is called asynchronously, so be careful not to have side effects.
         *
         * @return A predicate to check if the location is a Load Scope.
         */
        BiPredicate<ResourceManager, ResourceLocation> provideLoadScopePredicate();

    }

    public interface AsyncLoadScope {

        /**
         * Must return a CompletableFuture that obtains a Predicate to check if the given resource location is Load Scope.<br/>
         * his method is called from the rendering thread, but the Predicate test is called asynchronously, so be careful not to have side effects.
         *
         * @param resourceManager Resource Manager
         * @param executor        Executor
         * @return A CompletableFuture that takes a predicate to check if the location is a load scope.
         */
        CompletableFuture<Predicate<ResourceLocation>> provideAsyncLoadScopePredicate(ResourceManager resourceManager, Executor executor);

    }
}
