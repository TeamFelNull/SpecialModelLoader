package dev.felnull.ifoltest.block;

import dev.felnull.ifoltest.IFOLTest;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class IFOLTestBlocks {
    public static final Block NORMAL_MODEL_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST, MaterialColor.DEEPSLATE));
    public static final Block OBJ_MODEL_BLOCK = new Block(FabricBlockSettings.of(Material.AMETHYST, MaterialColor.DEEPSLATE));

    public static void init() {

        register("normal_model_block", NORMAL_MODEL_BLOCK);
        register("obj_model_block", OBJ_MODEL_BLOCK);

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            if (group == CreativeModeTabs.BUILDING_BLOCKS) {
                entries.accept(NORMAL_MODEL_BLOCK);
                entries.accept(OBJ_MODEL_BLOCK);
            }
        });
    }

    public static void register(String name, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(IFOLTest.MODID, name), block);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(IFOLTest.MODID, name), new BlockItem(block, new FabricItemSettings()));
    }
}
