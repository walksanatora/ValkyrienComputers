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
	private MotorBlockEntity motor;

	public MotorPeripheral(MotorBlockEntity be) {
		this.motor = be;
		this.level = be.getLevel();
		this.pos = be.getBlockPos();
	}

	@LuaFunction
	public final boolean activate() throws LuaException {
		if (level.isClientSide()) return false;

		doesMotorStillExist();
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

		doesMotorStillExist();
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
		if (motor.getHingeId() == null) return false;

		doesMotorStillExist();
		motor.destroyConstraints();
		return true;
	}

	@LuaFunction
	public final boolean createHead() throws LuaException {
		if (level.isClientSide()) return false;

		doesMotorStillExist();
		motor.makeOrGetTop((ServerLevel) level, pos);
		return true;
	}

	@LuaFunction
	public final boolean hasHead() throws LuaException {
		if (level.isClientSide()) return false;

		doesMotorStillExist();
		return motor.getAttachmentConstraintId() != null && motor.getHingeId() != null;
	}

	@LuaFunction
	public final double getAngle() throws LuaException {
		if (level.isClientSide()) return 0.0;

		doesMotorStillExist();
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

	public void doesMotorStillExist() {
		motor.setChanged();
	}
}
