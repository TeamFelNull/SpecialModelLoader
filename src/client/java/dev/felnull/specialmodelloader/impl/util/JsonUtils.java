package dev.felnull.specialmodelloader.impl.util;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;

public class JsonUtils {
    public static Identifier getParentLocation(JsonObject modelJson) {
        if (modelJson == null)
            return null;

        if (modelJson.has("parent"))
            return Identifier.parse(GsonHelper.getAsString(modelJson, "parent"));
        return null;
    }

    public static Identifier getResourceLocation(JsonObject jsonObject, String name) {
        if (jsonObject.has(name) && jsonObject.get(name).isJsonPrimitive()
                && jsonObject.getAsJsonPrimitive(name).isString()) {
            return Identifier.parse(jsonObject.get(name).getAsString());
        }
        return null;
    }
}
