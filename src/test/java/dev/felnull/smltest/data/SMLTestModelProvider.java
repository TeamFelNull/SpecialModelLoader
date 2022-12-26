package dev.felnull.smltest.data;

import dev.felnull.smltest.SMLTest;
import dev.felnull.smltest.block.SMLTestBlocks;
import dev.felnull.smltest.item.SMLTestItems;
import dev.felnull.specialmodelloader.api.data.SpecialModelDataGenHelper;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class SMLTestModelProvider extends FabricModelProvider {
    public static final ModelTemplate OBJ = new ModelTemplate(Optional.of(new ResourceLocation(SpecialModelLoader.MODID, "builtin/obj")), Optional.empty());

    public SMLTestModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(SMLTestBlocks.NORMAL_MODEL_BLOCK);

        createFacing(blockStateModelGenerator, SMLTestBlocks.FACING_MODEL_BLOCK);

        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(SMLTestBlocks.FACING_OBJ_MODEL_BLOCK, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(SMLTestBlocks.OBJ_MODEL_BLOCK, "")))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    private void createFacing(BlockModelGenerators blockStateModelGenerator, Block block) {
        var r = TexturedModel.ORIENTABLE_ONLY_TOP.create(SMLTestBlocks.FACING_MODEL_BLOCK, blockStateModelGenerator.modelOutput);

        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, r))
                .with(BlockModelGenerators.createHorizontalFacingDispatch()));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(SMLTestItems.NORMAL_MODEL_ITEM, ModelTemplates.FLAT_ITEM);
        SpecialModelDataGenHelper.generateObjModel(SMLTestItems.OBJ_MODEL_ITEM, new ResourceLocation(SMLTest.MODID, "models/item/sea_cheken_pack/sea_chicken.obj"), true, false, null, itemModelGenerator.output);
    }
}
