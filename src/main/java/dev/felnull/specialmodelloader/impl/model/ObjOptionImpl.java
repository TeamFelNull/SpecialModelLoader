package dev.felnull.specialmodelloader.impl.model;

import dev.felnull.specialmodelloader.api.ObjOption;

public record ObjOptionImpl(boolean flipV) implements ObjOption {
    @Override
    public boolean isFlipV() {
        return flipV;
    }
}
