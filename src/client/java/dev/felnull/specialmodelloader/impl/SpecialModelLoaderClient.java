package dev.felnull.specialmodelloader.impl;

import dev.felnull.specialmodelloader.impl.handler.SMLClientHandler;
import dev.felnull.specialmodelloader.impl.handler.SMLModelLoadingHandler;
import dev.felnull.specialmodelloader.impl.model.NeoForgeCompat;
import net.fabricmc.api.ClientModInitializer;

public class SpecialModelLoaderClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SMLClientHandler.init();
        SMLModelLoadingHandler.init();
        NeoForgeCompat.init();
    }
}
