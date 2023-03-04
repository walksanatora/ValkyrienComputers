package net.techtastic.vc

import dev.architectury.platform.Platform
import net.techtastic.vc.integrations.cc.ComputerCraftWeights

object ValkyrienComputerWeights {
    fun register() {
        if (Platform.isModLoaded("computercraft")) {
            ComputerCraftWeights.register()
        }
    }
}