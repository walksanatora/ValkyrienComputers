package net.techtastic.vc.integrations.cc.valkyrienskies;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.techtastic.vc.blockentity.MotorBlockEntity;
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MotorPeripheral implements IPeripheral {
	private Level level;
	private BlockPos pos;

	public MotorPeripheral(Level level, BlockPos worldPosition) {
		this.level = level;
		this.pos = worldPosition;
	}

	@LuaFunction
	public final boolean activate() throws LuaException {
		if (level.isClientSide()) return false;
		if (!(level.getBlockEntity(pos) instanceof MotorBlockEntity motor)) return false;

		if (motor.getActivated()) {
			motor.setActivated(false);
			return true;
		}
		motor.setActivated(true);
		return true;
	}

	@LuaFunction
	public final boolean reverse() throws LuaException {
		if (level.isClientSide()) return false;
		if (!(level.getBlockEntity(pos) instanceof MotorBlockEntity motor)) return false;

		if (motor.getReversed()) {
			motor.setReversed(false);
			return true;
		}
		motor.setReversed(true);
		return true;
	}

	@LuaFunction
	public final boolean removeHead() throws LuaException {
		if (level.isClientSide()) return false;
		if (!(level.getBlockEntity(pos) instanceof MotorBlockEntity motor)) return false;
		if (motor.getHingeId() == null) return false;

		motor.destroyConstraints();
		return true;
	}

	@LuaFunction
	public final boolean createHead() throws LuaException {
		if (level.isClientSide()) return false;
		if (!(level.getBlockEntity(pos) instanceof MotorBlockEntity motor)) return false;
		if (motor.getHingeId() != null) return false;

		motor.makeOrGetTop((ServerLevel) level, pos);
		return true;
	}

	@LuaFunction
	public final double getAngle() throws LuaException {
		if (level.isClientSide()) return 0.0;
		System.err.println("We are Serverside!!!");
		System.err.println("BlockEntity: " + level.getBlockEntity(pos));
		if (!(level.getBlockEntity(pos) instanceof MotorBlockEntity motor)) return 0.0;
		System.err.println("Motor exists!!!!!");

		return motor.getCurrentAngle();
	}

	@NotNull
	@Override
	public String getType() {
		return "motor";
	}

	@Override
	public boolean equals(@Nullable IPeripheral iPeripheral) {
		return level.getBlockState(pos).is(ComputerCraftBlocks.MOTOR.get());
	}
}
