package dev.felnull.specialmodelloader.impl.model.obj;

import com.google.gson.JsonObject;
import de.javagl.obj.Mtl;
import de.javagl.obj.MtlReader;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import dev.felnull.specialmodelloader.impl.mixin.BlockModelAccessor;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjModelLoaderImp implements ObjModelLoader {
    /* private static final ResourceLocation REPLACE_TARGET = new ResourceLocation(SpecialModelLoader.MODID, "builtin/obj");
     private static final ResourceLocation REPLACE_TARGET_2 = new ResourceLocation("sml", "builtin/obj");

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
         return loadModel(resourceManager, modelLocation, BlockModelAccessor.getGson().fromJson(jo.get("display"), ItemTransforms.class), ObjModelOption.of(true));
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
     public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ItemTransforms transforms, @NotNull ObjModelOption option) throws ModelProviderException {
         var res = resourceManager.getResource(location);
         if (res.isEmpty())
             return null;

         try (var reader = res.get().openAsReader()) {
             var obj = ObjUtils.convertToRenderable(ObjReader.read(reader));

             String[] paths = location.getPath().split("/");
             paths = ArrayUtils.remove(paths, paths.length - 1);
             var loc = new ResourceLocation(location.getNamespace(), String.join("/", paths));

             return new ObjUnbakedModelModel(obj, loadMtl(resourceManager, loc, obj.getMtlFileNames()), transforms, option);
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
                 SpecialModelLoader.LOGGER.error("Failed to read mtl file.", e);
                 return new ArrayList<Mtl>();
             }
         }).orElseGet(List::of);
     }*/
    @Override
    public @Nullable UnbakedModel loadModel(@NotNull ResourceManager resourceManager, @NotNull JsonObject modelJson) throws ModelProviderException {
        if (!modelJson.has("model") || !modelJson.get("model").isJsonPrimitive() || !modelJson.getAsJsonPrimitive("model").isString())
            return null;

        var modelLocation = new ResourceLocation(modelJson.get("model").getAsString());
        return loadModel_(resourceManager, modelLocation, BlockModelAccessor.getGson().fromJson(modelJson.get("display"), ItemTransforms.class), ObjModelOption.parse(modelJson));
    }

    private @Nullable UnbakedModel loadModel_(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation location, @NotNull ItemTransforms transforms, @NotNull ObjModelOption option) throws ModelProviderException {
        var res = resourceManager.getResource(location);
        if (res.isEmpty())
            return null;

        try (var reader = res.get().openAsReader()) {
            var obj = ObjUtils.convertToRenderable(ObjReader.read(reader));

            String[] paths = location.getPath().split("/");
            paths = ArrayUtils.remove(paths, paths.length - 1);
            var loc = new ResourceLocation(location.getNamespace(), String.join("/", paths));

            return new ObjUnbakedModelModel(obj, loadMtl(resourceManager, loc, obj.getMtlFileNames()), transforms, option);
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
                SpecialModelLoader.LOGGER.error("Failed to read mtl file.", e);
                return new ArrayList<Mtl>();
            }
        }).orElseGet(List::of);
    }

    @Override
    public String getId() {
        return "obj";
    }
}
