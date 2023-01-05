package net.techtastic.vc

import dev.architectury.platform.Platform
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.techtastic.vc.registry.DeferredRegister
import net.techtastic.vc.registry.RegistrySupplier

@Suppress("unused")
object ValkyrienComputersBlocks {
    private val BLOCKS = DeferredRegister.create(ValkyrienComputersMod.MOD_ID, Registry.BLOCK_REGISTRY)

    //val FARTER = BLOCKS.register("farter", ::FartBlock)
    //val BEARING = BLOCKS.register("bearing", ::BearingBaseBlock)
    //val BEARING_TOP = BLOCKS.register("bearing_top", ::BearingTopBlock)
    // endregion

    var RADAR = if (Platform.getMod("computercraft") != null) BLOCKS.register("radar_cc") { Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)) } else null

    var READER = if (Platform.getMod("computercraft") != null) BLOCKS.register("reader_cc") { Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)) } else null

    fun register() {
        BLOCKS.applyAll()
    }

    // Blocks should also be registered as items, if you want them to be able to be held
    // aka all blocks
    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
            items.register(it.name) { BlockItem(it.get(), Item.Properties().tab(ValkyrienComputersItems.TAB)) }
        }
    }

}
