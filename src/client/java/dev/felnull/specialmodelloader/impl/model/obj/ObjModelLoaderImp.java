package dev.felnull.specialmodelloader.impl.model.obj;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import de.javagl.obj.*;
import dev.felnull.specialmodelloader.api.model.LoadedResource;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import dev.felnull.specialmodelloader.impl.SpecialModelLoaderClient;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjModelLoaderImp implements ObjModelLoader {
    public static final ObjModelLoaderImp INSTANCE = new ObjModelLoaderImp();

    @Override
    public @Nullable LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull JsonObject modelJson) {
        if (!modelJson.has("model") || !modelJson.get("model").isJsonPrimitive() || !modelJson.getAsJsonPrimitive("model").isString()) {
            return null;
        }

        ResourceLocation modelLocation = ResourceLocation.parse(modelJson.get("model").getAsString());

        return loadResource(resourceManager, modelLocation, ObjModelOption.parse(modelJson));
    }

    @Override
    public @Nullable LoadedResource loadResource(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation modelLocation, @NotNull ObjModelOption option) {
        Optional<Resource> res = resourceManager.getResource(modelLocation);

        if (res.isEmpty()) {
            return null;
        }

        try (var reader = res.get().openAsReader()) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(reader));

            ResourceLocation mtlDirLoc;
            List<String> mtlFileNames;

            String mtlOverride;
            if ((mtlOverride = option.getMtlOverride()) != null) {
                String[] overrideSplit = mtlOverride.split("/");

                mtlDirLoc = ResourceLocation.parse(String.join("/", ArrayUtils.remove(overrideSplit, overrideSplit.length - 1)));
                mtlFileNames = ImmutableList.of(overrideSplit[overrideSplit.length - 1]);
            } else {
                String[] mtlDirPaths = modelLocation.getPath().split("/");
                mtlDirPaths = ArrayUtils.remove(mtlDirPaths, mtlDirPaths.length - 1);

                mtlDirLoc = ResourceLocation.fromNamespaceAndPath(modelLocation.getNamespace(), String.join("/", mtlDirPaths));
                mtlFileNames = obj.getMtlFileNames();
            }

            return new ObjModelLoadedResource(modelLocation, obj,
                    ImmutableMap.copyOf(loadMtl(resourceManager, mtlDirLoc, mtlFileNames)), option);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load obj file.", e);
        }
    }

    @Override
    public @NotNull UnbakedModel makeModel(@NotNull LoadedResource loadedResource) {
        if (loadedResource instanceof ObjModelLoadedResource objRes) {
            return new ObjUnbakedModelModel(objRes.location(), objRes.obj(), objRes.mtl(), objRes.option());
        } else {
            throw new IllegalArgumentException("A loaded resource that is not an OBJ model was received as an argument.");
        }
    }

    private Map<String, Mtl> loadMtl(ResourceManager resourceManager, ResourceLocation location, List<String> mtlNames) {
        return mtlNames.stream()
                .flatMap(r -> loadMtl(resourceManager, location, r).stream())
                .collect(Collectors.toMap(Mtl::getName, r -> r));
    }

    private List<Mtl> loadMtl(ResourceManager resourceManager, ResourceLocation location, String mtlName) {
        var loc = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), location.getPath() + "/" + mtlName);
        return resourceManager.getResource(loc).map(res -> {
            try (var reader = res.openAsReader()) {
                return MtlReader.read(reader);
            } catch (IOException e) {
                SpecialModelLoaderClient.LOGGER.error("Failed to read mtl file.", e);
                return new ArrayList<Mtl>();
            }
        }).orElseGet(List::of);
    }

    @Override
    public String getId() {
        return "obj";
    }
}
