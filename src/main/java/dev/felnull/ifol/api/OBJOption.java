package dev.felnull.ifol.api;

import dev.felnull.ifol.impl.model.OBJOptionImpl;
import org.jetbrains.annotations.NotNull;

public interface OBJOption {
    @NotNull
    static OBJOption of(boolean flipV) {
        return new OBJOptionImpl(flipV);
    }

    boolean isFlipV();
}
