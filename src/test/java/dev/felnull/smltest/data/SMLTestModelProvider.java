package dev.felnull.smltest.data;

import com.google.common.collect.ImmutableMap;
import dev.felnull.smltest.SMLTest;
import dev.felnull.smltest.block.SMLTestBlocks;
import dev.felnull.smltest.item.SMLTestItems;
import dev.felnull.specialmodelloader.api.data.SpecialModelDataGenHelper;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SMLTestModelProvider extends FabricModelProvider {

        public SMLTestModelProvider(FabricDataOutput output) {
                super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
                blockStateModelGenerator.createTrivialCube(SMLTestBlocks.NORMAL_MODEL_BLOCK);

                createFacing(blockStateModelGenerator, SMLTestBlocks.FACING_MODEL_BLOCK);

                blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator
                                .dispatch(SMLTestBlocks.FACING_OBJ_MODEL_BLOCK,
                                                new MultiVariant(WeightedList.of(new Variant(ModelLocationUtils
                                                                .getModelLocation(SMLTestBlocks.OBJ_MODEL_BLOCK, "")))))
                                .with(createHorizontalFacingDispatch()));
        }

        private void createFacing(BlockModelGenerators blockStateModelGenerator, Block block) {
                var r = TexturedModel.ORIENTABLE_ONLY_TOP.create(SMLTestBlocks.FACING_MODEL_BLOCK,
                                blockStateModelGenerator.modelOutput);

                blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
                                new MultiVariant(WeightedList.of(new Variant(r))))
                                .with(createHorizontalFacingDispatch()));
        }

        private static PropertyDispatch<VariantMutator> createHorizontalFacingDispatch() {
                return PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                                .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                                .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                                .select(Direction.WEST, BlockModelGenerators.Y_ROT_270)
                                .select(Direction.NORTH, BlockModelGenerators.NOP);
        }

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {
                itemModelGenerator.generateFlatItem(SMLTestItems.NORMAL_MODEL_ITEM, ModelTemplates.FLAT_ITEM);

                SpecialModelDataGenHelper.generateObjModel(SMLTestItems.OBJ_MODEL_ITEM,
                                Identifier.fromNamespaceAndPath(SMLTest.MODID,
                                                "models/item/sea_cheken_pack/sea_chicken.obj"),
                                true, false, null, ImmutableMap.of(),
                                null, itemModelGenerator.modelOutput);
                itemModelGenerator.itemModelOutput.accept(SMLTestItems.OBJ_MODEL_ITEM,
                                ItemModelUtils.plainModel(
                                                ModelLocationUtils.getModelLocation(SMLTestItems.OBJ_MODEL_ITEM)));

                SpecialModelDataGenHelper.generateObjModel(SMLTestItems.MTLOVERRIDE_OBJ_MODEL_ITEM,
                                Identifier.fromNamespaceAndPath(SMLTest.MODID, "models/item/ring/ring.obj"),
                                true, false, SMLTest.MODID + ":models/item/ring/ring_2.mtl", ImmutableMap.of(),
                                null, itemModelGenerator.modelOutput);
                itemModelGenerator.itemModelOutput.accept(SMLTestItems.MTLOVERRIDE_OBJ_MODEL_ITEM,
                                ItemModelUtils.plainModel(ModelLocationUtils
                                                .getModelLocation(SMLTestItems.MTLOVERRIDE_OBJ_MODEL_ITEM)));

                SpecialModelDataGenHelper.generateObjModel(SMLTestItems.TEXTURES_OBJ_MODEL_ITEM,
                                Identifier.fromNamespaceAndPath(SMLTest.MODID, "models/item/ring/ring.obj"),
                                true, false, null,
                                ImmutableMap.of("texture0", Identifier.withDefaultNamespace("block/cobblestone")),
                                null, itemModelGenerator.modelOutput);
                itemModelGenerator.itemModelOutput.accept(SMLTestItems.TEXTURES_OBJ_MODEL_ITEM,
                                ItemModelUtils.plainModel(ModelLocationUtils
                                                .getModelLocation(SMLTestItems.TEXTURES_OBJ_MODEL_ITEM)));
        }
}
