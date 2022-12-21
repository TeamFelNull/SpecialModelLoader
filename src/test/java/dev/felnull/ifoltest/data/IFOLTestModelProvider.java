package dev.felnull.ifoltest.data;

import dev.felnull.ifoltest.block.IFOLTestBlocks;
import dev.felnull.ifoltest.item.IFOLTestItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;

public class IFOLTestModelProvider extends FabricModelProvider {
    public IFOLTestModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(IFOLTestBlocks.NORMAL_MODEL_BLOCK);

        createFacing(blockStateModelGenerator, IFOLTestBlocks.FACING_MODEL_BLOCK);

        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(IFOLTestBlocks.FACING_OBJ_MODEL_BLOCK, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(IFOLTestBlocks.OBJ_MODEL_BLOCK, "")))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    private void createFacing(BlockModelGenerators blockStateModelGenerator, Block block) {
        var r = TexturedModel.ORIENTABLE_ONLY_TOP.create(IFOLTestBlocks.FACING_MODEL_BLOCK, blockStateModelGenerator.modelOutput);

        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, r))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(IFOLTestItems.NORMAL_MODEL_ITEM, ModelTemplates.FLAT_ITEM);
    }
}
