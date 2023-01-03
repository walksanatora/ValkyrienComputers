package net.techtastic.vc.services

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.techtastic.vc.registry.DeferredRegister

interface DeferredRegisterBackend {
    fun <T> makeDeferredRegister(id: String, registry: ResourceKey<Registry<T>>): DeferredRegister<T>
}