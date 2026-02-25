package dev.felnull.smltest.item;

import dev.felnull.smltest.SMLTest;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class SMLTestItems {
    public static final Item NORMAL_MODEL_ITEM = register("normal_model_item", Item::new, new Item.Properties());
    public static final Item OBJ_MODEL_ITEM = register("obj_model_item", Item::new, new Item.Properties());
    public static final Item OBJ2_MODEL_ITEM = register("obj2_model_item", Item::new, new Item.Properties());
    public static final Item FORGE_OBJ_MODEL_ITEM = register("forge_obj_model_item", Item::new, new Item.Properties());
    public static final Item DYNAMIC_OBJ_MODEL_ITEM = register("dynamic_obj_model_item", Item::new,
            new Item.Properties());
    public static final Item MTLOVERRIDE_OBJ_MODEL_ITEM = register("mtloverride_obj_model_item", Item::new,
            new Item.Properties());
    public static final Item TEXTURES_OBJ_MODEL_ITEM = register("textures_obj_model_item", Item::new,
            new Item.Properties());

    public static void init() {
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

    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> resKey = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(SMLTest.MODID, name));
        return Items.registerItem(resKey, factory, properties);
    }
}
