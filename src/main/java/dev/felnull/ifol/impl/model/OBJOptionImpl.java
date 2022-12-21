package dev.felnull.ifol.impl.model;

import dev.felnull.ifol.api.OBJOption;

public record OBJOptionImpl(boolean flipV) implements OBJOption {
    @Override
    public boolean isFlipV() {
        return flipV;
    }
}
