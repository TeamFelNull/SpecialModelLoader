package dev.felnull.specialmodelloader.api;

import dev.felnull.specialmodelloader.impl.model.ObjOptionImpl;
import org.jetbrains.annotations.NotNull;

public interface ObjOption {
    @NotNull
    static ObjOption of(boolean flipV) {
        return new ObjOptionImpl(flipV);
    }

    boolean isFlipV();
}
