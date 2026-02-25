package dev.felnull.specialmodelloader.impl.model.obj;

import de.javagl.obj.Mtl;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjFace;
import de.javagl.obj.ObjSplitting;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import dev.felnull.specialmodelloader.impl.SpecialModelLoader;
import dev.felnull.specialmodelloader.impl.model.SpecialBaseUnbakedModel;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.MeshBakedGeometry;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;

import java.util.Map;

public class ObjUnbakedModelModel extends SpecialBaseUnbakedModel {
    private final Identifier location;
    private final Obj obj;
    private final Map<String, Mtl> mtl;
    private final ObjModelOption option;

    public ObjUnbakedModelModel(Identifier location, Obj obj, Map<String, Mtl> mtl, ObjModelOption option) {
        super(option);
        this.location = location;
        this.obj = obj;
        this.mtl = mtl;
        this.option = option;
    }

    @Override
    public UnbakedGeometry geometry() {
        return (textureSlots, baker, modelState, debugName) -> {
            Renderer renderer = Renderer.get();

            if (renderer == null) {
                SpecialModelLoader.LOGGER.warn("IndigoRenderer is used since the Renderer cannot be obtained. ({})",
                        location);
                renderer = IndigoRenderer.INSTANCE;
            }

            MutableMesh builder = renderer.mutableMesh();
            QuadEmitter emitter = builder.emitter();
            SpriteGetter spriteGetter = baker.sprites();
            Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);

            materialGroups.forEach((name, model) -> {
                for (int i = 0; i < model.getNumFaces(); i++) {
                    emitFace(emitter, modelState, spriteGetter, debugName, name, model, model.getFace(i));
                }
            });

            return new MeshBakedGeometry(builder.immutableCopy());
        };
    }

    private void emitFace(QuadEmitter emitter, ModelState modelState, SpriteGetter spriteGetter,
            ModelDebugName debugName, String materialName, Obj fObj, ObjFace face) {
        for (int i = 0; i < face.getNumVertices(); i++) {
            emitVertex(i, i, emitter, modelState, fObj, face);
        }

        if (face.getNumVertices() == 3)
            emitVertex(3, 2, emitter, modelState, fObj, face);

        var smtl = mtl.get(materialName);

        int flg = MutableQuadView.BAKE_NORMALIZED;

        if (option.isFlipV())
            flg |= MutableQuadView.BAKE_FLIP_V;

        Identifier texLoc = null;
        String tex;
        if (smtl != null && (tex = smtl.getMapKd()) != null) {
            if (tex.startsWith("#")) {
                texLoc = option.getTextures().get(tex.substring(1));
            } else {
                texLoc = Identifier.parse(tex);
            }
        }

        if (texLoc != null) {
            emitter.spriteBake(spriteGetter.get(new Material(TextureAtlas.LOCATION_BLOCKS, texLoc), debugName), flg);
        } else {
            emitter.spriteBake(spriteGetter.get(MISSING, debugName), flg);
        }

        emitter.color(-1, -1, -1, -1);

        emitter.emit();
    }

    private void emitVertex(int index, int vertexNum, QuadEmitter emitter, ModelState modelState, Obj fObj,
            ObjFace face) {
        var vt = fObj.getVertex(face.getVertexIndex(vertexNum));
        var vertex = new Vector3f(vt.getX(), vt.getY(), vt.getZ());

        vertex.add(-0.5f, -0.5f, -0.5f);
        vertex.rotate(modelState.transformation().getLeftRotation());
        vertex.add(0.5f, 0.5f, 0.5f);

        var normal = fObj.getNormal(face.getNormalIndex(vertexNum));
        var tex = fObj.getTexCoord(face.getTexCoordIndex(vertexNum));

        emitter.pos(index, vertex.x(), vertex.y(), vertex.z())
                .normal(index, normal.getX(), normal.getY(), normal.getZ())
                .uv(index, tex.getX(), tex.getY());
    }
}
