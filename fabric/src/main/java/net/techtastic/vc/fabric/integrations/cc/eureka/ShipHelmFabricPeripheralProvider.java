package net.techtastic.vc.fabric.integrations.cc.eureka;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.eureka.block.ShipHelmBlock;

public class ShipHelmFabricPeripheralProvider implements IPeripheralProvider {
    @NotNull
    @Override
    public IPeripheral getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        if (
                level.getBlockState(blockPos).getBlock() instanceof ShipHelmBlock
        ) {
            return new ShipHelmPeripheral(level, blockPos);
        }
        return null;
    }
}
