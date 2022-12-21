package dev.felnull.ifoltest.data;

import dev.felnull.ifoltest.block.IFOLTestBlocks;
import dev.felnull.ifoltest.item.IFOLTestItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class IFOLTestModelProvider extends FabricModelProvider {
    public IFOLTestModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(IFOLTestBlocks.NORMAL_MODEL_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(IFOLTestItems.NORMAL_MODEL_ITEM, ModelTemplates.FLAT_ITEM);
    }
}
