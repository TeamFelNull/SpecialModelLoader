package dev.felnull.specialmodelloader.impl.util;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class JsonModelUtils {
    public static ResourceLocation getParentLocation(JsonObject modelJson) {
        if (modelJson == null)
            return null;

        if (modelJson.has("parent"))
            return new ResourceLocation(GsonHelper.getAsString(modelJson, "parent"));
        return null;
    }
}
