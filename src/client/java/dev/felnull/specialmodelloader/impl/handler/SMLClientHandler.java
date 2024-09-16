package dev.felnull.specialmodelloader.impl.handler;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.Reader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SMLClientHandler {
    private static final Gson GSON = new Gson();
    private static final String MANUAL_LOAD_SCOPE_RESOURCE_NAME = "sml_load_scopes";

    public static void init() {
        SpecialModelLoaderEvents.LOAD_SCOPE_ASYNC.register(SMLClientHandler::provideSimpleLoadScopePredicate);
        SpecialModelLoaderEvents.LOAD_SCOPE_ASYNC.register(SMLClientHandler::provideManualLoadScopePredicate);
    }

    private static CompletableFuture<Predicate<ResourceLocation>> provideSimpleLoadScopePredicate(ResourceManager resourceManager, Executor executor) {
        BiPredicate<ResourceManager, ResourceLocation> predicate = SpecialModelLoaderEvents.LOAD_SCOPE.invoker().provideLoadScopePredicate();
        return CompletableFuture.supplyAsync(() -> loc -> predicate.test(resourceManager, loc), executor);
    }

    private static CompletableFuture<Predicate<ResourceLocation>> provideManualLoadScopePredicate(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {

            ImmutableSet.Builder<Pattern> patternsBuilder = new ImmutableSet.Builder<>();

            resourceManager.listResources(MANUAL_LOAD_SCOPE_RESOURCE_NAME, loc -> loc.getPath().endsWith(".json")).forEach((location, resource) -> {
                try (Reader reader = resource.openAsReader()) {
                    JsonObject jo = GSON.fromJson(reader, JsonObject.class);
                    loadManualLoadScope(patternsBuilder, jo);
                } catch (Exception e) {
                    SpecialModelLoader.LOGGER.error("Error occurred while loading model load scope resource json {}", location, e);
                }
            });

            final ImmutableSet<Pattern> patterns = patternsBuilder.build();
            int size = patterns.size();
            if (size >= 1) {
                SpecialModelLoader.LOGGER.info("Loaded {} manual model load scope", size);
            }

            return loc -> patterns.stream()
                    .anyMatch(pattern -> pattern.matcher(loc.toString()).matches());
        }, executor);
    }

    private static void loadManualLoadScope(ImmutableSet.Builder<Pattern> builder, JsonObject jsonObject) {

        if (!jsonObject.has("version") || !jsonObject.get("version").isJsonPrimitive() || !jsonObject.getAsJsonPrimitive("version").isNumber()) {
            throw new IllegalStateException("Unknown version");
        }

        int version = jsonObject.getAsJsonPrimitive("version").getAsInt();

        if (version != 1) {
            throw new IllegalStateException("Unsupported version");
        }

        if (!jsonObject.has("entry") || !jsonObject.get("entry").isJsonArray()) {
            return;
        }

        JsonArray entries = jsonObject.getAsJsonArray("entry");
        for (JsonElement entry : entries) {
            if (entry.isJsonPrimitive() && entry.getAsJsonPrimitive().isString()) {
                builder.add(Pattern.compile(entry.getAsString()));
            }
        }
    }
}
