package dev.felnull.specialmodelloader.api.model.obj;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.impl.model.obj.ObjModelOptionImpl;
import org.jetbrains.annotations.NotNull;

public interface ObjModelOption extends ModelOption {
    @NotNull
    static ObjModelOption of(@NotNull ModelOption modelOption, boolean flipV) {
        return new ObjModelOptionImpl(modelOption, flipV);
    }

    @NotNull
    static ObjModelOption parse(@NotNull JsonObject modelJson) {
        return ObjModelOptionImpl.parse(modelJson);
    }

    boolean isFlipV();
}
