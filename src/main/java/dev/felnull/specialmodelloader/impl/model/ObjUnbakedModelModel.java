package dev.felnull.specialmodelloader.impl.model;

import de.javagl.obj.Mtl;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjSplitting;
import dev.felnull.specialmodelloader.api.ObjOption;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ObjUnbakedModelModel implements UnbakedModel {
    private static final Material MISSING = new Material(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation());
    private final Obj obj;
    private final Map<String, Mtl> mtl;
    private final ItemTransforms itemTransforms;
    private final ObjOption option;

    public ObjUnbakedModelModel(Obj obj, Map<String, Mtl> mtl, ItemTransforms itemTransforms, ObjOption option) {
        this.obj = obj;
        this.mtl = mtl;
        this.itemTransforms = itemTransforms;
        this.option = option;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return List.of();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> function) {
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> textureGetter, ModelState modelState, ResourceLocation resourceLocation) {
        final Renderer renderer = RendererAccess.INSTANCE.getRenderer() != null ? RendererAccess.INSTANCE.getRenderer() : IndigoRenderer.INSTANCE;

        MeshBuilder builder = renderer.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);

        materialGroups.forEach((name, model) -> {
            for (int i = 0; i < model.getNumFaces(); i++) {
                emitFace(emitter, modelState, textureGetter, name, model, model.getFace(i));
            }
        });

        return new ObjModel(builder.build(), itemTransforms, textureGetter.apply(MISSING));
    }

    private void emitFace(QuadEmitter emitter, ModelState modelState, Function<Material, TextureAtlasSprite> textureGetter, String materialName, Obj fObj, ObjFace face) {
        for (int i = 0; i < face.getNumVertices(); i++) {
            emitVertex(i, i, emitter, modelState, fObj, face);
        }

        if (face.getNumVertices() == 3)
            emitVertex(3, 2, emitter, modelState, fObj, face);

        var smtl = mtl.get(materialName);

        int flg = MutableQuadView.BAKE_NORMALIZED;

        if (option.isFlipV())
            flg |= MutableQuadView.BAKE_FLIP_V;

        if (modelState.isUvLocked())
            flg |= MutableQuadView.BAKE_LOCK_UV;

        if (smtl != null && smtl.getMapKd() != null) {
            emitter.spriteBake(0, textureGetter.apply(new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(smtl.getMapKd()))), flg);
        } else {
            emitter.spriteBake(0, textureGetter.apply(MISSING), flg);
        }


        emitter.spriteColor(0, -1, -1, -1, -1);

        emitter.emit();
    }

    private void emitVertex(int index, int vertexNum, QuadEmitter emitter, ModelState modelState, Obj fObj, ObjFace face) {
        var vt = fObj.getVertex(face.getVertexIndex(vertexNum));
        var vertex = new Vector3f(vt.getX(), vt.getY(), vt.getZ());

        vertex.add(-0.5f, -0.5f, -0.5f);
        vertex.rotate(modelState.getRotation().getLeftRotation());
        vertex.add(0.5f, 0.5f, 0.5f);

        var normal = fObj.getNormal(face.getNormalIndex(vertexNum));
        var tex = fObj.getTexCoord(face.getTexCoordIndex(vertexNum));

        emitter.pos(index, vertex.x(), vertex.y(), vertex.z())
                .normal(index, normal.getX(), normal.getY(), normal.getZ())
                .sprite(index, 0, new Vec2(tex.getX(), tex.getY()));
    }
}
