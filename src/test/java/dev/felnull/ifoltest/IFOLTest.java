package dev.felnull.ifoltest;

import dev.felnull.ifoltest.item.IFOLTestItems;
import net.fabricmc.api.ModInitializer;

public class IFOLTest implements ModInitializer {
    public static final String MODID = "ikisugi-fabric-obj-loader-test";

    @Override
    public void onInitialize() {
        IFOLTestItems.init();
    }
}
