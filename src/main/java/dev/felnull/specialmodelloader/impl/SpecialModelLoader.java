package dev.felnull.specialmodelloader.impl;

import dev.felnull.specialmodelloader.impl.handler.SMLModelResourceHandler;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpecialModelLoader implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(SpecialModelLoader.class);
    public static final String MODID = "special-model-loader";

    @Override
    public void onInitializeClient() {
        SMLModelResourceHandler.init();
    }
}
