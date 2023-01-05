package net.techtastic.vc

import dev.architectury.platform.Platform
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks
import net.techtastic.vc.registry.CreativeTabs
import net.techtastic.vc.registry.DeferredRegister
import net.techtastic.vc.registry.RegistrySupplier

@Suppress("unused")
object ValkyrienComputersItems {
    val ITEMS = DeferredRegister.create(ValkyrienComputersMod.MOD_ID, Registry.ITEM_REGISTRY)
    val TAB: CreativeModeTab = CreativeTabs.create(
        ResourceLocation(
            ValkyrienComputersMod.MOD_ID,
            "vc_tab"
        )
    ) { ItemStack(LOGO.get()) }

    var LOGO: RegistrySupplier<Item> = ITEMS.register("vc_logo") { Item(Item.Properties()) }

    fun register() {
        ValkyrienComputersBlocks.registerItems(ITEMS)
        if (Platform.isModLoaded("computercraft")) ComputerCraftBlocks.registerItems(ITEMS)
        ITEMS.applyAll()
    }

    private infix fun Item.byName(name: String) = ITEMS.register(name) { this }
}
