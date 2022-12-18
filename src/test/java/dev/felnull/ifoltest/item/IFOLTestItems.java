package dev.felnull.ifoltest.item;

import dev.felnull.ifoltest.IFOLTest;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class IFOLTestItems {
    public static final Item NORMAL_MODEL_ITEM = new Item(new FabricItemSettings());

    public static void init() {
        register("normal_model_item", NORMAL_MODEL_ITEM);

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            if (group == CreativeModeTabs.BUILDING_BLOCKS) {
                entries.accept(NORMAL_MODEL_ITEM);
            }
        });
    }

    public static void register(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(IFOLTest.MODID, name), item);
    }
}
