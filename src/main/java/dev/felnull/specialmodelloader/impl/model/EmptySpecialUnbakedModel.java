package dev.felnull.specialmodelloader.impl.model;

import com.google.common.collect.ImmutableSet;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmptySpecialUnbakedModel extends SpecialBaseUnbakedModel {
    private ResourceLocation parentModelLocation;
    private UnbakedModel parentModel;

    public EmptySpecialUnbakedModel(ModelOption modelOption, ResourceLocation parentModelLocation) {
        super(modelOption);
        this.parentModelLocation = parentModelLocation;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        if (parentModelLocation == null)
            return ImmutableSet.of();
        return ImmutableSet.of(parentModelLocation);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> function) {
        Set<UnbakedModel> loadedModels = new HashSet<>();

        for (EmptySpecialUnbakedModel model = this; model.parentModel instanceof EmptySpecialUnbakedModel; model = (EmptySpecialUnbakedModel) model.parentModel) {
            loadedModels.add(model);

            var pareModel = function.apply(model.parentModelLocation);

            if (!(pareModel instanceof SpecialBaseUnbakedModel)) {
                SpecialModelLoader.LOGGER.warn("No loader '{}' failed to load '{}'", this.parentModelLocation, model);
                pareModel = null;
            }

            if (pareModel == null)
                SpecialModelLoader.LOGGER.warn("No parent '{}' while loading model '{}'", this.parentModelLocation, model);

            if (loadedModels.contains(pareModel)) {
                SpecialModelLoader.LOGGER.warn("Found 'parent' loop while loading model '{}' in chain: {} -> {}", pareModel, loadedModels.stream().map(Object::toString).collect(Collectors.joining(" -> ")), this.parentModelLocation);
                pareModel = null;
            }

            if (pareModel == null) {
                model.parentModelLocation = ModelBakery.MISSING_MODEL_LOCATION;
                pareModel = function.apply(model.parentModelLocation);
            }

            model.parentModel = pareModel;
        }
    }

    @Override
    public BlockModel.GuiLight getGuiLight() {
        if (parentModel instanceof SpecialBaseUnbakedModel specialBaseUnbakedModel)
            return specialBaseUnbakedModel.getGuiLight();
        return getModelOption().getGuiLight() != null ? getModelOption().getGuiLight() : BlockModel.GuiLight.SIDE;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState, ResourceLocation resourceLocation) {
        return new SpecialEmptyModel(getModelOption().isUseAmbientOcclusion(), getGuiLight().lightLikeBlock(), function.apply(new Material(InventoryMenu.BLOCK_ATLAS, getModelOption().getParticle())), getModelOption().getTransforms());
    }
}
