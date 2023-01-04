package net.techtastic.vc.integrations.cc;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.registry.RegistrySupplier;

public class ComputerCraftBlocks {
    public static RegistrySupplier<Block> RADAR;
    public static RegistrySupplier<Block> READER;

    public static RegistrySupplier<Block> registerBlock(String name, Block block, DeferredRegister<Block> BLOCKS) {
        return BLOCKS.register(name, () -> block);
    }

    public static void registerCCBlocks(DeferredRegister<Block> BLOCKS) {
        RADAR = registerBlock("radar_cc", new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)), BLOCKS);
        READER = registerBlock("reader_cc", new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)), BLOCKS);
        BLOCKS.applyAll();
    }
}
