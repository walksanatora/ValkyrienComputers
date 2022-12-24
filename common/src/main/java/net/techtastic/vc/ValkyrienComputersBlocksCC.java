package net.techtastic.vc;

import dan200.computercraft.shared.ComputerCraftRegistry;
import dan200.computercraft.shared.media.items.ItemDisk;
import dan200.computercraft.shared.util.CreativeTabMain;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.Iterator;

public class ValkyrienComputersBlocksCC {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ValkyrienComputersMod.MOD_ID, Registry.BLOCK_REGISTRY);
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ValkyrienComputersMod.MOD_ID, Registry.ITEM_REGISTRY);

    public static RegistrySupplier<Block> RADAR;
    public static RegistrySupplier<Block> READER;

    public static RegistrySupplier<Block> registerBlock(String name, Block block, CreativeModeTab tab) {
        registerBlockItem(name, block, tab);
        return BLOCKS.register(name, () -> block);
    }

    public static void registerBlockItem(String name, Block block, CreativeModeTab tab) {
        ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(tab)));
    }

    public static void registerCCBlocks(CreativeModeTab tab) {
        BLOCKS.register();
        ITEMS.register();

        ValkyrienComputersConfig.Server.COMPUTERCRAFT config = ValkyrienComputersConfig.SERVER.getComputerCraft();
        if (!config.getDisableRadars()) {
            RADAR = registerBlock("radar_cc", new Block(BlockBehaviour.Properties.of(Material.METAL)), tab);
        }
        if (!config.getDisableShipReaders()) {
            READER = registerBlock("reader_cc", new Block(BlockBehaviour.Properties.of(Material.METAL)), tab);
        }
    }
}
