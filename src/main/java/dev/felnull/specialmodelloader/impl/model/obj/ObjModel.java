package dev.felnull.specialmodelloader.impl.model.obj;

import com.google.common.base.Suppliers;
import dev.felnull.specialmodelloader.impl.model.SpecialBaseModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ObjModel extends SpecialBaseModel {
    private final Mesh mesh;
    private final Supplier<List<BakedQuad>[]> quadCache;

    public ObjModel(boolean useAmbientOcclusion, boolean usesBlockLight, TextureAtlasSprite particleIcon, ItemTransforms transforms, Mesh mesh) {
        super(useAmbientOcclusion, usesBlockLight, particleIcon, transforms);
        this.mesh = mesh;
        this.quadCache = Suppliers.memoize(() -> ModelHelper.toQuadLists(mesh));
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.meshConsumer().accept(mesh);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return this.quadCache.get()[ModelHelper.toFaceIndex(direction)];
    }
}
