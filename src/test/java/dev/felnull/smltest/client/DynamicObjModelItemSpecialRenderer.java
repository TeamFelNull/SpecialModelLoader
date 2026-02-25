package dev.felnull.smltest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class DynamicObjModelItemSpecialRenderer implements NoDataSpecialModelRenderer {

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        consumer.accept(new Vector3f(0, 0, 0));
        consumer.accept(new Vector3f(1, 1, 1));
    }

    @Override
    public void submit(ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector,
            int packedLight, int packedOverlay, boolean hasFoilType, int seed) {
        poseStack.pushPose();

        poseCenterConsumer(poseStack, 0.5f, 0.5f, 0.5f, pose -> {
            poseRotateX(poseStack, 360f * (float) (System.currentTimeMillis() % 10000) / 10000f);
            poseRotateY(poseStack, 360f * (float) (System.currentTimeMillis() % 20000) / 20000f);
            poseRotateZ(poseStack, 360f * (float) (System.currentTimeMillis() % 30000) / 30000f);
        });

        var model = ((FabricBakedModelManager) Minecraft.getInstance().getModelManager())
                .getModel(SMLTestClient.TEST_OBJ_MODEL_KEY);
        if (model != null) {
            collector.submitBlockModel(poseStack, Sheets.solidBlockSheet(), model, 1.0f, 1.0f, 1.0f, packedLight,
                    packedOverlay, seed);
        }

        poseStack.popPose();
    }

    public static void poseCenterConsumer(@NotNull PoseStack poseStack, float centerX, float centerY, float centerZ,
            @NotNull Consumer<PoseStack> poseStackConsumer) {
        poseStack.translate(centerX, centerY, centerZ);
        poseStackConsumer.accept(poseStack);
        poseStack.translate(-centerX, -centerY, -centerZ);
    }

    public static void poseRotateX(@NotNull PoseStack poseStack, float angle) {
        poseStack.mulPose(Axis.XP.rotationDegrees(angle));
    }

    public static void poseRotateY(@NotNull PoseStack poseStack, float angle) {
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
    }

    public static void poseRotateZ(@NotNull PoseStack poseStack, float angle) {
        poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @NotNull SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new DynamicObjModelItemSpecialRenderer();
        }

        @Override
        public @NotNull MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
