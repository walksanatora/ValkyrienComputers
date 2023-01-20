package net.techtastic.vc.integrations.cc;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.techtastic.vc.ValkyrienComputersMod;
import net.techtastic.vc.blockentity.MotorBlockEntity;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.registry.RegistrySupplier;

public class ComputerCraftBlockEntities {
    private static DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister.Companion.create(ValkyrienComputersMod.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

    public static RegistrySupplier<BlockEntityType> MOTOR = BLOCKENTITIES.register("motor_cc", () -> BlockEntityType.Builder.of(
            MotorBlockEntity::new,
            ComputerCraftBlocks.MOTOR.get()
    ).build(null));

    public static void registerBlockEntities() {
        BLOCKENTITIES.applyAll();
    }
}
