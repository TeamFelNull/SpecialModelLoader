package dev.felnull.specialmodelloader.impl.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public final class ForgeObjModelCompat {
    private static final ResourceLocation FORGE_OBJ = ResourceLocation.fromNamespaceAndPath("forge", "obj");

    public static Pair<ResourceLocation, ObjModelOption> getObjModelData(JsonObject modelJson) {
        if (modelJson == null)
            return null;

        if (!modelJson.has("loader") || !modelJson.get("loader").isJsonPrimitive() || !modelJson.getAsJsonPrimitive("loader").isString() || !FORGE_OBJ.toString().equals(modelJson.get("loader").getAsString()))
            return null;

        var model = ResourceLocation.parse(modelJson.get("model").getAsString());
        return Pair.of(model, ObjModelOption.parse(modelJson));
    }
}
