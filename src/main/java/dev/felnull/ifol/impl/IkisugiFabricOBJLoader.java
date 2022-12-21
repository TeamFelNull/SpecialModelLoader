package dev.felnull.ifol.impl;

import dev.felnull.ifol.impl.handler.IFOLModelResourceHandler;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IkisugiFabricOBJLoader implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(IkisugiFabricOBJLoader.class);
    public static final String MODID = "ikisugi-fabric-obj-loader";

    @Override
    public void onInitializeClient() {
        IFOLModelResourceHandler.init();
    }
}
