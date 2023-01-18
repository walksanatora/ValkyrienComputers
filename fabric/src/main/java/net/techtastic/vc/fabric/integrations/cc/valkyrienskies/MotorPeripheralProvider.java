package net.techtastic.vc.fabric.integrations.cc.valkyrienskies;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks;
import net.techtastic.vc.integrations.cc.valkyrienskies.MotorPeripheral;
import org.jetbrains.annotations.NotNull;

public class MotorPeripheralProvider implements IPeripheralProvider {
	@Override
	public IPeripheral getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
		/*if (
				level.getBlockState(blockPos).is(ComputerCraftBlocks.MOTOR.get())
		) {
			return new MotorPeripheral(level, blockPos);
		}*/
		return null;
	}
}