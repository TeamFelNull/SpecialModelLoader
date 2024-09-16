package dev.felnull.specialmodelloader.impl;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.SpecialModelLoaderAPI;
import dev.felnull.specialmodelloader.api.model.LoadedResource;
import dev.felnull.specialmodelloader.api.model.ModelLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import dev.felnull.specialmodelloader.impl.model.ForgeObjModelCompat;
import dev.felnull.specialmodelloader.impl.model.obj.ObjModelLoaderImp;
import dev.felnull.specialmodelloader.impl.util.JsonModelUtils;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.*;

public class SpecialModelLoaderAPIImpl implements SpecialModelLoaderAPI {
    public static final SpecialModelLoaderAPIImpl INSTANCE = new SpecialModelLoaderAPIImpl();
    private final List<ModelLoader> loaders = ImmutableList.of(ObjModelLoaderImp.INSTANCE);

    @Override
    public @NotNull ObjModelLoader getObjLoader() {
        return ObjModelLoaderImp.INSTANCE;
    }

    @Override
    public @Nullable LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation) {
        List<JsonObject> models = new ArrayList<>();
        JsonObject jo = readJson(resourceManager, modelLocation);

        Pair<ResourceLocation, ObjModelOption> forgeModel = ForgeObjModelCompat.getObjModelData(jo);
        if (forgeModel != null) {
            return getObjLoader().loadResource(resourceManager, forgeModel.getLeft(), forgeModel.getRight());
        }

        ResourceLocation location = JsonModelUtils.getParentLocation(jo);
        Set<ResourceLocation> parents = new HashSet<>();

        while (location != null) {
            models.add(jo);

            if (parents.contains(location)) {
                SpecialModelLoader.LOGGER.warn("Model parent specification is looping: '{}', '{}'", modelLocation, location);
                return null;
            }

            parents.add(location);

            ModelLoader loader = getLoader(location);
            if (loader != null) {
                JsonObject ret = new JsonObject();
                Collections.reverse(models);
                models.forEach(r -> r.asMap().forEach((name, rlm) -> ret.add(name, rlm.deepCopy())));
                return loader.loadResource(resourceManager, ret);
            }

            jo = readJson(resourceManager, location);
            location = JsonModelUtils.getParentLocation(jo);
        }

        return null;
    }

    @Override
    public @NotNull UnbakedModel makeModel(@NotNull LoadedResource resource) {
        return resource.getLoader().makeModel(resource);
    }

    @Override
    public @Unmodifiable @NotNull List<ModelLoader> getLoaders() {
        return loaders;
    }

    private ModelLoader getLoader(ResourceLocation location) {
        return getLoaders().stream()
                .filter(r -> r.isLoaderLocation(location))
                .limit(1)
                .findFirst()
                .orElse(null);
    }

    private JsonObject readJson(ResourceManager resourceManager, ResourceLocation modelLocation) {
        ResourceLocation modelPath = ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), "models/" + modelLocation.getPath() + ".json");
        var res = resourceManager.getResource(modelPath);
        if (res.isEmpty()) return null;
        JsonObject jo;
        try (var reader = res.get().openAsReader()) {
            jo = GsonHelper.parse(reader);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load json: " + modelLocation, ex);
        }
        return jo;
    }
}
