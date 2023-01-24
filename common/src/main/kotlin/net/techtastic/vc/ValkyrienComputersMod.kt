package net.techtastic.vc

import dan200.computercraft.api.ComputerCraftAPI
import net.minecraft.resources.ResourceLocation
import org.valkyrienskies.core.impl.config.VSConfigClass

object ValkyrienComputersMod {
    const val MOD_ID = "vc"

    @JvmStatic
    fun init() {
        ValkyrienComputersBlocks.register()
        ValkyrienComputersBlockEntities.register()
        ValkyrienComputersItems.register()
        VSConfigClass.registerConfig("vc", ValkyrienComputersConfig::class.java)
    }

    @JvmStatic
    fun initClient() {
    }

    val String.resource: ResourceLocation get() = ResourceLocation(MOD_ID, this)
}