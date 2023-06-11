package dev.felnull.smltest.block;

import dev.felnull.smltest.SMLTest;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class SMLTestBlocks {
    public static final Block NORMAL_MODEL_BLOCK = new Block(FabricBlockSettings.create().sounds(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));
    public static final Block OBJ_MODEL_BLOCK = new Block(FabricBlockSettings.create().sounds(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));
    public static final Block FACING_MODEL_BLOCK = new FacingBlock(FabricBlockSettings.create().sounds(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));
    public static final Block FACING_OBJ_MODEL_BLOCK = new FacingBlock(FabricBlockSettings.create().sounds(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));

    public static void init() {

        register("normal_model_block", NORMAL_MODEL_BLOCK);
        register("obj_model_block", OBJ_MODEL_BLOCK);
        register("facing_model_block", FACING_MODEL_BLOCK);
        register("facing_obj_model_block", FACING_OBJ_MODEL_BLOCK);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(ct -> {
            ct.accept(NORMAL_MODEL_BLOCK);
            ct.accept(OBJ_MODEL_BLOCK);
            ct.accept(FACING_MODEL_BLOCK);
            ct.accept(FACING_OBJ_MODEL_BLOCK);
        });
    }

    public static void register(String name, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(SMLTest.MODID, name), block);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(SMLTest.MODID, name), new BlockItem(block, new FabricItemSettings()));
    }
}
