package dev.felnull.specialmodelloader.impl.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.SpecialModelLoaderAPI;
import dev.felnull.specialmodelloader.api.model.EmptyModelLoader;
import dev.felnull.specialmodelloader.api.model.SpecialBaseLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpecialModelLoaderAPIImpl implements SpecialModelLoaderAPI {
    public static final SpecialModelLoaderAPIImpl INSTANCE = new SpecialModelLoaderAPIImpl();
    private final ObjModelLoader objLoader = new ObjModelLoaderImp();
    private final EmptyModelLoader emptyLoader = new EmptySpecialLoaderImpl();
    private final List<SpecialBaseLoader> useLoaders = ImmutableList.of(emptyLoader, objLoader);

    @Override
    public @NotNull ObjModelLoader getObjLoader() {
        return objLoader;
    }

    @Override
    public @NotNull EmptyModelLoader getEmptyLoader() {
        return emptyLoader;
    }

    @Override
    public @Unmodifiable @NotNull List<SpecialBaseLoader> getUseLoaders() {
        return useLoaders;
    }

    @Override
    public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation) throws ModelProviderException {
        var bjo = readJson(resourceManager, modelLocation);

       // System.out.println(modelLocation);

        var myLoader = getLoader(modelLocation);
        if (myLoader != null)
            return bjo == null ? null : myLoader.loadModel(resourceManager, bjo);

        JsonObject jo = bjo;
        ResourceLocation location = JsonModelUtils.getParentLocation(jo);
        Set<ResourceLocation> aly = new HashSet<>();

        while (location != null) {
            if (aly.contains(location)) {
                SpecialModelLoader.LOGGER.warn("Model parent specification is looping: {}", modelLocation);
                return null;
            }
            aly.add(location);

            if (getLoader(location) != null)
                return getEmptyLoader().loadModel(resourceManager, bjo);

            jo = readJson(resourceManager, modelLocation);
            location = JsonModelUtils.getParentLocation(jo);
        }

        return null;
    }

    private SpecialBaseLoader getLoader(ResourceLocation location) {
        return useLoaders.stream()
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
