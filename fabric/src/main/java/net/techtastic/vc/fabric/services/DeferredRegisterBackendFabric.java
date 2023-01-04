package net.techtastic.vc.fabric.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.services.DeferredRegisterBackend;
import org.jetbrains.annotations.NotNull;
import net.techtastic.vc.fabric.DeferredRegisterImpl;

public class DeferredRegisterBackendFabric implements DeferredRegisterBackend {

    @Override
    public @NotNull <T> DeferredRegister<T> makeDeferredRegister(@NotNull String id, @NotNull ResourceKey<Registry<T>> registry) {
        return new DeferredRegisterImpl<>(id, registry);
    }
}
