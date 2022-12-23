package dev.felnull.specialmodelloader.impl.model;

import dev.felnull.specialmodelloader.api.model.ModelOption;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.world.inventory.InventoryMenu;

public abstract class SpecialBaseUnbakedModel implements UnbakedModel {
    protected static final Material MISSING = new Material(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation());
    private final ModelOption modelOption;

    protected SpecialBaseUnbakedModel(ModelOption modelOption) {
        this.modelOption = modelOption;
    }

    public ModelOption getModelOption() {
        return modelOption;
    }

    public Material getParticleLocation() {
        if (modelOption.getParticle() != null)
            return new Material(InventoryMenu.BLOCK_ATLAS, modelOption.getParticle());
        return MISSING;
    }

    public abstract BlockModel.GuiLight getGuiLight();
}
