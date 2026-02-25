package dev.felnull.smltest.client;

import dev.felnull.smltest.SMLTest;
import dev.felnull.specialmodelloader.api.event.SpecialModelLoaderEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;

public class SMLTestClient implements ClientModInitializer {
    public static final Identifier TEST_OBJ_MODEL = Identifier.fromNamespaceAndPath(SMLTest.MODID,
            "item/obj_model_item_dynamic");
    public static final ExtraModelKey<BlockStateModel> TEST_OBJ_MODEL_KEY = ExtraModelKey.create();

    @Override
    public void onInitializeClient() {
        SpecialModelLoaderEvents.LOAD_SCOPE
                .register(() -> (resManager, location) -> SMLTest.MODID.equals(location.getNamespace()));
        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModel(TEST_OBJ_MODEL_KEY,
                SimpleUnbakedExtraModel.blockStateModel(TEST_OBJ_MODEL)));

        SpecialModelRenderers.ID_MAPPER.put(
                Identifier.fromNamespaceAndPath(SMLTest.MODID, "dynamic_obj_model_item"),
                DynamicObjModelItemSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
