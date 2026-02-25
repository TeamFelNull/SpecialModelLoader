package dev.felnull.smltest.block;

import dev.felnull.smltest.SMLTest;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class SMLTestBlocks {
    public static final Block NORMAL_MODEL_BLOCK = register("normal_model_block", Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));
    public static final Block OBJ_MODEL_BLOCK = register("obj_model_block", Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));
    public static final Block FACING_MODEL_BLOCK = register("facing_model_block", FacingBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));
    public static final Block FACING_OBJ_MODEL_BLOCK = register("facing_obj_model_block", FacingBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).mapColor(MapColor.DEEPSLATE));

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(ct -> {
            ct.accept(NORMAL_MODEL_BLOCK);
            ct.accept(OBJ_MODEL_BLOCK);
            ct.accept(FACING_MODEL_BLOCK);
            ct.accept(FACING_OBJ_MODEL_BLOCK);
        });
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties properties) {
        ResourceKey<Block> resKey = ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(SMLTest.MODID, name));
        Block block = Blocks.register(resKey, factory, properties);
        Items.registerBlock(block);
        return block;
    }
}
