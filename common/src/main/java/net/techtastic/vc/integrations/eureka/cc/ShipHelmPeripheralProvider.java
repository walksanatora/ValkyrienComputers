package net.techtastic.vc.integrations.eureka.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.techtastic.vc.integrations.eureka.cc.ShipHelmPeripheral;
import org.jetbrains.annotations.NotNull;
import net.techtastic.vc.ValkyrienComputersConfig;
import org.valkyrienskies.eureka.block.ShipHelmBlock;

public class ShipHelmPeripheralProvider implements IPeripheralProvider {
    @NotNull
    @Override
    public IPeripheral getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
        if (
                level.getBlockState(blockPos).getBlock() instanceof ShipHelmBlock &&
                        !ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableComputerCraft()
        ) {
            return new ShipHelmPeripheral(level, blockPos);
        }
        return null;
    }
}
