package dev.felnull.smltest.client;

import dev.felnull.smltest.SMLTest;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import net.fabricmc.api.ClientModInitializer;

public class SMLTestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpecialModelLoaderEvents.LOAD_SCOPE.register(location -> SMLTest.MODID.equals(location.getNamespace()));
    }
}
