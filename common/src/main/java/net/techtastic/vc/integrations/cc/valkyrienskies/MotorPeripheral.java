package net.techtastic.vc.integrations.cc.valkyrienskies;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.ValkyrienComputersConfig.Server.COMPUTERCRAFT.RADARSETTINGS;
import net.techtastic.vc.blockentity.MotorBlockEntity;
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.apigame.constraints.*;
import org.valkyrienskies.core.apigame.world.ServerShipWorldCore;
import org.valkyrienskies.core.impl.game.ships.ShipObjectServerWorld;
import org.valkyrienskies.core.impl.hooks.VSEvents;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.HashMap;
import java.util.List;

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

		List<Vector3d> ships = VSGameUtilsKt.transformToNearbyShipsAndWorld(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5);
		for (Vector3d vec : ships) {
			ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, vec.x, vec.y, vec.z);
			ServerShipWorldCore sswc = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level);
		}

		return true;
	}

	@LuaFunction
	public final boolean reverse() throws LuaException {
		if (level.isClientSide()) return false;



		return true;
	}

	@NotNull
	@Override
	public String getType() {
		return "motor";
	}

	@Override
	public boolean equals(@Nullable IPeripheral iPeripheral) {
		return false;//level.getBlockState(pos).is(ComputerCraftBlocks.MOTOR.get());
	}
}
