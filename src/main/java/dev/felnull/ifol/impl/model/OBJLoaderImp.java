package dev.felnull.ifol.impl.model;

import com.google.gson.JsonObject;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlReader;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import dev.felnull.ifol.api.OBJLoader;
import dev.felnull.ifol.api.OBJOption;
import dev.felnull.ifol.impl.IkisugiFabricOBJLoader;
import dev.felnull.ifol.impl.mixin.BlockModelAccessor;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OBJLoaderImp implements OBJLoader {
    private static final ResourceLocation REPLACE_TARGET = new ResourceLocation(IkisugiFabricOBJLoader.MODID, "builtin/obj");
    private static final ResourceLocation REPLACE_TARGET_2 = new ResourceLocation("ifol", "builtin/obj");

    public static final OBJLoaderImp INSTANCE = new OBJLoaderImp();

    @Override
    public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location) throws ModelProviderException {
        JsonObject jo;
        try {
            jo = getIntegratedModelJson(resourceManager, location);
        } catch (IOException e) {
            throw new ModelProviderException("Json integration failed", e);
        }

        if (jo == null)
            return null;

        if (!jo.has("model") || !jo.get("model").isJsonPrimitive() || !jo.getAsJsonPrimitive("model").isString())
            return null;

        var modelLocation = new ResourceLocation(jo.get("model").getAsString());
        return loadModel(resourceManager, modelLocation, BlockModelAccessor.getGson().fromJson(jo.get("display"), ItemTransforms.class), OBJOption.of(true));
    }

    private JsonObject getIntegratedModelJson(ResourceManager resourceManager, ResourceLocation modelLocation) throws IOException {
        List<JsonObject> allParent = new ArrayList<>();

        ResourceLocation modelPath = new ResourceLocation(modelLocation.getNamespace(), "models/" + modelLocation.getPath() + ".json");

        while (modelPath != null) {
            var res = resourceManager.getResource(modelPath);
            if (res.isEmpty()) return null;

            JsonObject jo;
            try (var reader = res.get().openAsReader()) {
                jo = GsonHelper.parse(reader);
            }

            if (jo.has("parent") && jo.get("parent").isJsonPrimitive() && jo.getAsJsonPrimitive("parent").isString()) {
                var modelId = new ResourceLocation(jo.get("parent").getAsString());

                if (isObjTarget(modelId)) {
                    modelPath = null;
                } else {
                    modelPath = new ResourceLocation(modelId.getNamespace(), "models/" + modelId.getPath() + ".json");
                }
                allParent.add(jo);
            } else {
                return null;
            }
        }

        JsonObject ret = new JsonObject();

        Collections.reverse(allParent);
        allParent.forEach(r -> r.asMap().forEach((name, rlm) -> ret.add(name, rlm.deepCopy())));

        return ret;
    }

    private boolean isObjTarget(ResourceLocation location) {
        return REPLACE_TARGET.equals(location) || REPLACE_TARGET_2.equals(location);
    }

    @Override
    public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ItemTransforms transforms, @NotNull OBJOption option) throws ModelProviderException {
        var res = resourceManager.getResource(location);
        if (res.isEmpty())
            return null;

        try (var reader = res.get().openAsReader()) {
            var obj = ObjUtils.convertToRenderable(ObjReader.read(reader));

            String[] paths = location.getPath().split("/");
            paths = ArrayUtils.remove(paths, paths.length - 1);
            var loc = new ResourceLocation(location.getNamespace(), String.join("/", paths));
            //return new OBJUnbakedModelModelOld(obj, loadMtl(resourceManager, loc, obj.getMtlFileNames()), transforms, option);
            return new OBJUnbakedModelModel(obj, loadMtl(resourceManager, loc, obj.getMtlFileNames()), transforms, option);
        } catch (IOException e) {
            throw new ModelProviderException("Failed to load obj file", e);
        }
    }

    private Map<String, Mtl> loadMtl(ResourceManager resourceManager, ResourceLocation location, List<String> mtlNames) {
        return mtlNames.stream().flatMap(r -> loadMtl(resourceManager, location, r).stream()).collect(Collectors.toMap(Mtl::getName, r -> r));
    }

    private List<Mtl> loadMtl(ResourceManager resourceManager, ResourceLocation location, String mtlName) {
        var loc = new ResourceLocation(location.getNamespace(), location.getPath() + "/" + mtlName);
        return resourceManager.getResource(loc).map(res -> {
            try (var reader = res.openAsReader()) {
                return MtlReader.read(reader);
            } catch (IOException e) {
                IkisugiFabricOBJLoader.LOGGER.error("Failed to read mtl file.", e);
                return new ArrayList<Mtl>();
            }
        }).orElseGet(List::of);
    }
}
