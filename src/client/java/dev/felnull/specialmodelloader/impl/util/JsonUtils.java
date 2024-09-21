package dev.felnull.specialmodelloader.impl.util;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class JsonUtils {
    public static ResourceLocation getParentLocation(JsonObject modelJson) {
        if (modelJson == null)
            return null;

        if (modelJson.has("parent"))
            return ResourceLocation.parse(GsonHelper.getAsString(modelJson, "parent"));
        return null;
    }

    public static ResourceLocation getResourceLocation(JsonObject jsonObject, String name) {
        if (jsonObject.has(name) && jsonObject.get(name).isJsonPrimitive() && jsonObject.getAsJsonPrimitive(name).isString()) {
            return ResourceLocation.parse(jsonObject.get(name).getAsString());
        }
        return null;
    }
}
