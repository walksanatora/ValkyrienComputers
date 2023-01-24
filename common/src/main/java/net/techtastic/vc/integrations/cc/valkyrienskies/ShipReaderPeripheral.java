package net.techtastic.vc.integrations.cc.valkyrienskies;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;

public class ShipReaderPeripheral implements IPeripheral {

    private Level level;
    private BlockPos pos;

    public ShipReaderPeripheral(Level level, BlockPos worldPosition) {
        this.level = level;
        this.pos = worldPosition;
    }

    /*@LuaFunction
    public final String getShipName() throws LuaException {
        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                return ship.getName();
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return "";
    }

    @LuaFunction
    public final boolean setShipName(String string) throws LuaException {
        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                ship.setName(string);
                return true;
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return false;
    }*/

    @LuaFunction
    public final long getShipID() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                return ship.getId();
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return 0;
    }

    @LuaFunction
    public final double getMass() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                return ship.getInertiaData().getMass();
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return 0.0;
    }

    @LuaFunction
    public final Object[] getVelocity() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                Vector3dc vel = ship.getVelocity();
                return new Object[] { vel.x(), vel.y(), vel.z() };
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return new Object[0];
    }

    @LuaFunction
    public final Object[] getWorldspacePosition() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                Vector3dc vec = ship.getTransform().getPositionInWorld();
                return new Object[] { vec.x(), vec.y(), vec.z() };
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return new Object[0];
    }

    @LuaFunction
    public final Object[] getShipyardPosition() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                Vector3dc vec = ship.getTransform().getPositionInShip();
                return new Object[] { vec.x(), vec.y(), vec.z() };
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return new Object[0];
    }

    @LuaFunction
    public final Object[] getScale() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                Vector3dc scale = ship.getTransform().getShipToWorldScaling();
                return new Object[] { scale.x(), scale.y(), scale.z() };
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return new Object[0];
    }

    @LuaFunction
    public final Object[] getRotation() throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if (ship != null) {
                Quaterniondc rot = ship.getTransform().getShipToWorldRotation();
                return new Object[] { rot.x(), rot.y(), rot.z(), rot.w() };
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
        return new Object[0];
    }

    @LuaFunction
    public final Object[] transformPosition(double x, double y, double z) throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, new BlockPos(x, y, z));
            if (ship != null) {
                Vector3dc vec = ship.getShipToWorld().transformPosition(new Vector3d(x, y, z));
                return new Object[] { vec.x(), vec.y(), vec.z() };
            } else {
                ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
                if (ship != null) {
                    Vector3dc vec = ship.getWorldToShip().transformPosition(new Vector3d(x, y, z));
                    return new Object[] { vec.x(), vec.y(), vec.z() };
                } else {
                    throw new LuaException("Not on a Ship");
                }
            }
        }
        return new Object[0];
    }

    @LuaFunction
    public final Object[] transformDirection(double x, double y, double z) throws LuaException {
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");

        if (!level.isClientSide()) {
            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, new BlockPos(x, y, z));
            if (ship != null) {
                Vector3dc vec = ship.getShipToWorld().transformDirection(new Vector3d(x, y, z));
                return new Object[] { vec.x(), vec.y(), vec.z() };
            } else {
                ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
                if (ship != null) {
                    Vector3dc vec = ship.getWorldToShip().transformDirection(new Vector3d(x, y, z));
                    return new Object[] { vec.x(), vec.y(), vec.z() };
                } else {
                    throw new LuaException("Not on a Ship");
                }
            }
        }
        return new Object[0];
    }

    @NotNull
    @Override
    public String getType() {
        return "ship_reader";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return level.getBlockState(pos).is(ComputerCraftBlocks.READER.get());
    }
}
