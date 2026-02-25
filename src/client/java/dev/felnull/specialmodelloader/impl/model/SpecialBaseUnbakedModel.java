package dev.felnull.specialmodelloader.impl.model;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;

public abstract class SpecialBaseUnbakedModel implements UnbakedModel {
    protected static final Material MISSING = new Material(TextureAtlas.LOCATION_BLOCKS,
            MissingTextureAtlasSprite.getLocation());
    private final ModelOption modelOption;

    protected SpecialBaseUnbakedModel(ModelOption modelOption) {
        this.modelOption = modelOption;
    }

    public ModelOption getModelOption() {
        return modelOption;
    }

    public Material getParticleLocation() {
        if (modelOption.getParticle() != null)
            return new Material(TextureAtlas.LOCATION_BLOCKS, modelOption.getParticle());
        return MISSING;
    }

    public UnbakedModel.GuiLight getGuiLightValue() {
        if (modelOption.getGuiLight() == null)
            return UnbakedModel.GuiLight.SIDE;
        return modelOption.getGuiLight();
    }

    @Override
    public Boolean ambientOcclusion() {
        return modelOption.isUseAmbientOcclusion();
    }

    @Override
    public UnbakedModel.GuiLight guiLight() {
        return modelOption.getGuiLight();
    }

    @Override
    public ItemTransforms transforms() {
        return modelOption.getTransforms();
    }

    @Override
    public TextureSlots.Data textureSlots() {
        if (modelOption.getParticle() != null) {
            JsonObject jo = new JsonObject();
            jo.addProperty(UnbakedModel.PARTICLE_TEXTURE_REFERENCE, modelOption.getParticle().toString());
            return TextureSlots.parseTextureMap(jo);
        }
        return null;
    }

    @Override
    public Identifier parent() {
        return null;
    }
}
