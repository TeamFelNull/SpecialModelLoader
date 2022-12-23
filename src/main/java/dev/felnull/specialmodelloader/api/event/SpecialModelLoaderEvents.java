package dev.felnull.specialmodelloader.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

public final class SpecialModelLoaderEvents {
    public static final Event<LoadScope> LOAD_SCOPE = EventFactory.createArrayBacked(LoadScope.class, loadScopes -> location -> Arrays.stream(loadScopes).anyMatch(r -> r.isLoadScope(location)));

    public interface LoadScope {
        boolean isLoadScope(ResourceLocation location);
    }
}
