package dev.felnull.specialmodelloader.api.model.obj;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.impl.model.obj.ObjModelOptionImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public interface ObjModelOption extends ModelOption {

    @NotNull
    static ObjModelOption of(@NotNull ModelOption modelOption, boolean flipV,
                             String mtlOverride, Map<String, ResourceLocation> textures) {
        return new ObjModelOptionImpl(modelOption, flipV, mtlOverride, ImmutableMap.copyOf(textures));
    }

    @NotNull
    static ObjModelOption parse(@NotNull JsonObject modelJson) {
        return ObjModelOptionImpl.parse(modelJson);
    }

    boolean isFlipV();

    @Nullable
    String getMtlOverride();

    @Unmodifiable
    @NotNull
    Map<String, ResourceLocation> getTextures();
}
