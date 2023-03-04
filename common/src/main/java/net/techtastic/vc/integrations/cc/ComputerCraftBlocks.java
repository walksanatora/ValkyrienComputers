package net.techtastic.vc.integrations.cc;

import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.techtastic.vc.ValkyrienComputersItems;
import net.techtastic.vc.ValkyrienComputersMod;
import net.techtastic.vc.block.MotorBaseBlock;
import net.techtastic.vc.block.TopBlock;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.registry.RegistrySupplier;

public class ComputerCraftBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.Companion.create(ValkyrienComputersMod.MOD_ID, Registry.BLOCK_REGISTRY);

    public static RegistrySupplier<Block> RADAR = BLOCKS.register("radar_cc", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)));

    public static RegistrySupplier<Block> READER = BLOCKS.register("reader_cc", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)));

    public static RegistrySupplier<Block> MOTOR = BLOCKS.register("motor_cc", () -> new MotorBaseBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)));

    public static RegistrySupplier<Block> TOP = BLOCKS.register("top_cc", () -> new TopBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f)));

    public static void registerCCBlocks() {
        BLOCKS.applyAll();
    }

    public static void registerItems(DeferredRegister<Item> items) {
        for (RegistrySupplier<Block> block : BLOCKS) {
            if (block.equals(TOP)) {
                continue;
            }
            items.register(block.getName(), () -> new BlockItem(block.get(), new Item.Properties().tab(ValkyrienComputersItems.INSTANCE.getTAB())));
        }
    }
}
