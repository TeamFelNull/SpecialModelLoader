package dev.felnull.ifol.impl.mixin;

import com.google.gson.Gson;
import net.minecraft.client.renderer.block.model.BlockModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockModel.class)
public interface BlockModelAccessor {
    @Accessor("GSON")
    static Gson getGson() {
        throw new AssertionError();
    }
}

