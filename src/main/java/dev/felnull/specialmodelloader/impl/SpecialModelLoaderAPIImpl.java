package dev.felnull.specialmodelloader.impl;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.SpecialModelLoaderAPI;
import dev.felnull.specialmodelloader.api.model.SpecialBaseLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.impl.model.ForgeObjModelCompat;
import dev.felnull.specialmodelloader.impl.model.obj.ObjModelLoaderImp;
import dev.felnull.specialmodelloader.impl.util.JsonModelUtils;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.util.*;

public class SpecialModelLoaderAPIImpl implements SpecialModelLoaderAPI {
    public static final SpecialModelLoaderAPIImpl INSTANCE = new SpecialModelLoaderAPIImpl();
    private final ObjModelLoader objLoader = new ObjModelLoaderImp();
    private final List<SpecialBaseLoader> loaders = ImmutableList.of(objLoader);

    @Override
    public @NotNull ObjModelLoader getObjLoader() {
        return objLoader;
    }

    @Override
    public @Unmodifiable @NotNull List<SpecialBaseLoader> getLoaders() {
        return loaders;
    }

    @Override
    public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation) throws ModelProviderException {
        List<JsonObject> models = new ArrayList<>();
        JsonObject jo = readJson(resourceManager, modelLocation);

        var forgeModel = ForgeObjModelCompat.getObjModelData(jo);
        if (forgeModel != null)
            return getObjLoader().loadModel(resourceManager, forgeModel.getLeft(), forgeModel.getRight());

        ResourceLocation location = JsonModelUtils.getParentLocation(jo);
        Set<ResourceLocation> aly = new HashSet<>();

        while (location != null) {
            models.add(jo);

            if (aly.contains(location)) {
                SpecialModelLoader.LOGGER.warn("Model parent specification is looping: '{}', '{}'", modelLocation, location);
                return null;
            }

            aly.add(location);

            var loader = getLoader(location);
            if (loader != null) {
                JsonObject ret = new JsonObject();
                Collections.reverse(models);
                models.forEach(r -> r.asMap().forEach((name, rlm) -> ret.add(name, rlm.deepCopy())));
                return loader.loadModel(resourceManager, ret);
            }

            jo = readJson(resourceManager, location);
            location = JsonModelUtils.getParentLocation(jo);
        }

        return null;
    }

    private SpecialBaseLoader getLoader(ResourceLocation location) {
        return getLoaders().stream()
                .filter(r -> r.isUse(location))
                .limit(1)
                .findFirst()
                .orElse(null);
    }

    private JsonObject readJson(ResourceManager resourceManager, ResourceLocation modelLocation) throws ModelProviderException {
        ResourceLocation modelPath = new ResourceLocation(modelLocation.getNamespace(), "models/" + modelLocation.getPath() + ".json");
        var res = resourceManager.getResource(modelPath);
        if (res.isEmpty()) return null;
        JsonObject jo;
        try (var reader = res.get().openAsReader()) {
            jo = GsonHelper.parse(reader);
        } catch (IOException ex) {
            throw new ModelProviderException("Failed to load json: " + modelLocation, ex);
        }
        return jo;
    }
}
