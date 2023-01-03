package net.techtastic.vc.registry

interface RegistrySupplier<T> {

    val name: String
    fun get(): T

}