package dev.felnull.specialmodelloader.impl.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import dev.felnull.specialmodelloader.impl.util.JsonUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Compatibility to load models in formats supported by NeoForge
 */
public final class NeoForgeCompat {
    private static final ResourceLocation NEO_FORGE_OBJ = ResourceLocation.fromNamespaceAndPath("neoforge", "obj");

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("connector")) {
            SpecialModelLoader.LOGGER.info("Sinytra Connector detected.");
            SpecialModelLoader.LOGGER.info("Disable NeoForge compatibility for OBJ loader.");
        }
    }

    public static boolean isEnable() {
        // If the Sinytra Connector is present, disable compatibility as it was launched in NeoForge
        // https://sinytra.org/docs/connector/developers
        return !FabricLoader.getInstance().isModLoaded("connector");
    }

    public static Pair<ResourceLocation, ObjModelOption> getObjModelData(JsonObject modelJson) {
        if (modelJson == null) {
            return null;
        }

        // https://docs.neoforged.net/docs/resources/client/models/modelloaders/#obj-model

        ResourceLocation loaderLoc = JsonUtils.getResourceLocation(modelJson, "loader");
        if (!NEO_FORGE_OBJ.equals(loaderLoc)) {
            return null;
        }

        ResourceLocation model = JsonUtils.getResourceLocation(modelJson, "model");
        if (model == null) {
            return null;
        }

        return Pair.of(model, ObjModelOption.parse(modelJson));
    }
}
