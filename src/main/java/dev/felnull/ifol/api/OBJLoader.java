package dev.felnull.ifol.api;

import dev.felnull.ifol.impl.OBJLoaderImp;

public interface OBJLoader {
    static OBJLoader getInstance() {
        return OBJLoaderImp.INSTANCE;
    }
}
