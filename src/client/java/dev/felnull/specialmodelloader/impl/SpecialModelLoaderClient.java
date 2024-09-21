package dev.felnull.specialmodelloader.impl;

import dev.felnull.specialmodelloader.impl.handler.SMLClientHandler;
import dev.felnull.specialmodelloader.impl.handler.SMLModelLoadingHandler;
import dev.felnull.specialmodelloader.impl.model.NeoForgeCompat;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpecialModelLoaderClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(SpecialModelLoaderClient.class);

    @Override
    public void onInitializeClient() {
        SMLClientHandler.init();
        SMLModelLoadingHandler.init();
        NeoForgeCompat.init();
    }
}
