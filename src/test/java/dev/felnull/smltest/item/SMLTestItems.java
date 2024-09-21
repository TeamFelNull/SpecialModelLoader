package dev.felnull.smltest.item;

import dev.felnull.smltest.SMLTest;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class SMLTestItems {
    public static final Item NORMAL_MODEL_ITEM = new Item(new Item.Properties());
    public static final Item OBJ_MODEL_ITEM = new Item(new Item.Properties());
    public static final Item OBJ2_MODEL_ITEM = new Item(new Item.Properties());
    public static final Item FORGE_OBJ_MODEL_ITEM = new Item(new Item.Properties());
    public static final Item DYNAMIC_OBJ_MODEL_ITEM = new Item(new Item.Properties());
    public static final Item MTLOVERRIDE_OBJ_MODEL_ITEM = new Item(new Item.Properties());
    public static final Item TEXTURES_OBJ_MODEL_ITEM = new Item(new Item.Properties());

    public static void init() {
        register("normal_model_item", NORMAL_MODEL_ITEM);
        register("obj_model_item", OBJ_MODEL_ITEM);
        register("obj2_model_item", OBJ2_MODEL_ITEM);
        register("forge_obj_model_item", FORGE_OBJ_MODEL_ITEM);
        register("dynamic_obj_model_item", DYNAMIC_OBJ_MODEL_ITEM);
        register("mtloverride_obj_model_item", MTLOVERRIDE_OBJ_MODEL_ITEM);
        register("textures_obj_model_item", TEXTURES_OBJ_MODEL_ITEM);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(ct -> {
            ct.accept(NORMAL_MODEL_ITEM);
            ct.accept(OBJ_MODEL_ITEM);
            ct.accept(OBJ2_MODEL_ITEM);
            ct.accept(FORGE_OBJ_MODEL_ITEM);
            ct.accept(DYNAMIC_OBJ_MODEL_ITEM);
            ct.accept(MTLOVERRIDE_OBJ_MODEL_ITEM);
            ct.accept(TEXTURES_OBJ_MODEL_ITEM);
        });
    }

    public static void register(String name, Item item) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(SMLTest.MODID, name), item);
    }
}
