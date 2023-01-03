package net.techtastic.vc.cc;

import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.ValkyrienComputersMod;
import net.techtastic.vc.ValkyrienComputersTab;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.registry.RegistrySupplier;

public class ValkyrienComputersBlocksCC {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.Companion.create(ValkyrienComputersMod.MOD_ID, Registry.BLOCK_REGISTRY);
    public static DeferredRegister<Item> ITEMS = DeferredRegister.Companion.create(ValkyrienComputersMod.MOD_ID, Registry.ITEM_REGISTRY);

    public static RegistrySupplier<Block> RADAR;
    public static RegistrySupplier<Block> READER;

    public static RegistrySupplier<Block> registerBlock(String name, Block block) {
        return BLOCKS.register(name, () -> block);
    }

    public static void registerBlockItem(String name, RegistrySupplier<Block> block, CreativeModeTab tab) {
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void registerCCBlocks() {
        ValkyrienComputersConfig.Server.COMPUTERCRAFT config = ValkyrienComputersConfig.SERVER.getComputerCraft();
        if (!config.getDisableRadars()) {
            RADAR = registerBlock("radar_cc", new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)));
        }
        if (!config.getDisableShipReaders()) {
            READER = registerBlock("reader_cc", new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)));
        }

        BLOCKS.applyAll();

        CreativeModeTab tab = ValkyrienComputersTab.tab;

        registerBlockItem("radar_cc", RADAR, tab);
        registerBlockItem("reader_cc", READER, tab);

        ITEMS.applyAll();
    }
}
