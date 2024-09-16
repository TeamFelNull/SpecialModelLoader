package dev.felnull.specialmodelloader.impl.model.obj;

import de.javagl.obj.Mtl;
import de.javagl.obj.Obj;
import dev.felnull.specialmodelloader.api.model.LoadedResource;
import dev.felnull.specialmodelloader.api.model.ModelLoader;
import dev.felnull.specialmodelloader.api.model.obj.ObjModelOption;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

record ObjModelLoadedResource(ResourceLocation location,
                              Obj obj,
                              Map<String, Mtl> mtl,
                              ObjModelOption option) implements LoadedResource {
    @Override
    public ModelLoader getLoader() {
        return ObjModelLoaderImp.INSTANCE;
    }
}
