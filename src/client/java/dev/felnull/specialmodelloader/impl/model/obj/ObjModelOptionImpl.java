package dev.felnull.specialmodelloader.impl.model.obj;

import com.google.gson.JsonObject;
import dev.felnull.specialmodelloader.api.model.ModelOption;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ObjModelOptionImpl(ModelOption modelOption, boolean flipV) implements ObjModelOption {

    public static ObjModelOptionImpl parse(JsonObject modelJson) {
        return new ObjModelOptionImpl(ModelOption.parse(modelJson), GsonHelper.getAsBoolean(modelJson, "flip_v", false));
    }

    @Override
    public boolean isFlipV() {
        return flipV;
    }

    @Override
    public boolean isUseAmbientOcclusion() {
        return modelOption.isUseAmbientOcclusion();
    }

    @Override
    public @Nullable BlockModel.GuiLight getGuiLight() {
        return modelOption.getGuiLight();
    }

    @Override
    public @Nullable ResourceLocation getParticle() {
        return modelOption.getParticle();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return modelOption.getTransforms();
    }
}
