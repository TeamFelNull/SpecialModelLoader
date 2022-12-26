package dev.felnull.smltest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.felnull.smltest.SMLTest;
import dev.felnull.smltest.item.SMLTestItems;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class SMLTestClient implements ClientModInitializer {
    private static final Minecraft mc = Minecraft.getInstance();
    public static final ResourceLocation TEST_OBJ_MODEL = new ResourceLocation(SMLTest.MODID, "item/obj_model_item_dynamic");

    @Override
    public void onInitializeClient() {
        SpecialModelLoaderEvents.LOAD_SCOPE.register(location -> SMLTest.MODID.equals(location.getNamespace()));
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> out.accept(TEST_OBJ_MODEL));
        BuiltinItemRendererRegistry.INSTANCE.register(SMLTestItems.DYNAMIC_OBJ_MODEL_ITEM, (stack, mode, poseStack, vertexConsumers, light, overlay) -> {
            poseStack.pushPose();

            poseCenterConsumer(poseStack, 0.5f, 0.5f, 0.5f, pose -> {
                poseRotateX(poseStack, 360f * (float) (System.currentTimeMillis() % 10000) / 10000f);
                poseRotateY(poseStack, 360f * (float) (System.currentTimeMillis() % 20000) / 20000f);
                poseRotateZ(poseStack, 360f * (float) (System.currentTimeMillis() % 30000) / 30000f);
            });

            var model = BakedModelManagerHelper.getModel(mc.getModelManager(), TEST_OBJ_MODEL);
            var vc = vertexConsumers.getBuffer(Sheets.solidBlockSheet());
            renderModel(poseStack, vc, model, light, overlay);

            poseStack.popPose();
        });
    }

    public static void poseCenterConsumer(@NotNull PoseStack poseStack, float centerX, float centerY, float centerZ, @NotNull Consumer<PoseStack> poseStackConsumer) {
        poseStack.translate(centerX, centerY, centerZ);
        poseStackConsumer.accept(poseStack);
        poseStack.translate(-centerX, -centerY, -centerZ);
    }

    public static void renderModel(PoseStack poseStack, VertexConsumer vertexConsumer, @NotNull BakedModel bakedModel, int combinedLight, int combinedOverlay) {
        Objects.requireNonNull(bakedModel);
        var bmr = mc.getBlockRenderer().getModelRenderer();
        bmr.renderModel(poseStack.last(), vertexConsumer, null, bakedModel, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay);
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

}
