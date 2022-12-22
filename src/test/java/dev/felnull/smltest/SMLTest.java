package dev.felnull.smltest;

import dev.felnull.smltest.block.SMLTestBlocks;
import dev.felnull.smltest.item.SMLTestItems;
import net.fabricmc.api.ModInitializer;

public class SMLTest implements ModInitializer {
    public static final String MODID = "special-model-loader-test";

    @Override
    public void onInitialize() {
        SMLTestItems.init();
        SMLTestBlocks.init();
    }
}
