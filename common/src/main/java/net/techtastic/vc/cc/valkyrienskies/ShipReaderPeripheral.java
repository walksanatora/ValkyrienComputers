package net.techtastic.vc.cc.valkyrienskies;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import net.techtastic.vc.ValkyrienComputersConfig;
import org.joml.Quaterniondc;
import org.joml.primitives.AABBdc;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.techtastic.vc.cc.ValkyrienComputersBlocksCC;
import net.techtastic.vc.util.SpecialLuaTables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.valkyrienskies.core.game.ships.ShipData;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.HashMap;

public class ShipReaderPeripheral implements IPeripheral {

    private Level level;
    private BlockPos pos;

    public ShipReaderPeripheral(Level level, BlockPos worldPosition) {
        this.level = level;
        this.pos = worldPosition;
    }

    @LuaFunction
    public final String getShipName() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return ship.getName();
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final boolean setShipName(String string) throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            ship.setName(string);
            return true;
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final long getShipID() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return ship.getId();
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final double getMass() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return ship.getInertiaData().getShipMass();
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getVelocity() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getVelocity());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getWorldspacePosition() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getShipTransform().getShipPositionInWorldCoordinates());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getShipyardPosition() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getShipTransform().getShipPositionInShipCoordinates());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getScale() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getShipTransform().getShipCoordinatesToWorldCoordinatesScaling());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<String, Double> getSize() throws LuaException {
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            AABBdc aabb = ship.getShipAABB();
            HashMap<String, Double> map = new HashMap<>();
            map.put("x", aabb.maxX() - aabb.minX());
            map.put("Y", aabb.maxY() - aabb.minY());
            map.put("Z", aabb.maxZ() - aabb.minZ());
            return map;
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getRotation(IArguments arg) throws LuaException {
        boolean isQuaternion = arg.optBoolean(0, false);
        ShipData ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            if (isQuaternion) {
                return SpecialLuaTables.getQuaternionAsTable(ship.getShipTransform().getShipCoordinatesToWorldCoordinatesRotation());
            } else {
                HashMap<Object, Object> rpy = new HashMap<>();
                Quaterniondc quat = ship.getShipTransform().getShipCoordinatesToWorldCoordinatesRotation();
                double x = quat.x();
                double y = quat.y();
                double z = quat.z();
                double w = quat.w();
                rpy.put("roll", Math.atan2(2*y*w - 2*x*z, 1 - 2*y*y - 2*z*z));
                rpy.put("pitch", Math.atan2(2*x*w - 2*y*z, 1 - 2*x*x - 2*z*z));
                rpy.put("yaw", Math.asin(2*x*y + 2*z*w));
                return rpy;
            }
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> transformPosition(double x, double y, double z) throws LuaException {
        ShipData ship = isEnabledAndOnShip(new BlockPos(x, y, z));
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getShipToWorld().transformPosition(new Vector3d(x, y, z)));
        } else {
            ship = isEnabledAndOnShip(pos);
            if (ship != null) {
                return SpecialLuaTables.getVectorAsTable(ship.getWorldToShip().transformPosition(new Vector3d(x, y, z)));
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> transformDirection(double x, double y, double z) throws LuaException {
        ShipData ship = isEnabledAndOnShip(new BlockPos(x, y, z));
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getShipToWorld().transformDirection(new Vector3d(x, y, z)));
        } else {
            ship = isEnabledAndOnShip(pos);
            if (ship != null) {
                return SpecialLuaTables.getVectorAsTable(ship.getWorldToShip().transformDirection(new Vector3d(x, y, z)));
            } else {
                throw new LuaException("Not on a Ship");
            }
        }
    }

    public ShipData isEnabledAndOnShip(BlockPos position) throws LuaException {
        if (level.isClientSide()) return null;
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");
        return VSGameUtilsKt.getShipManagingPos((ServerLevel) level, position);
    }

    @NotNull
    @Override
    public String getType() {
        return "ship_reader";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return level.getBlockState(pos).getBlock().is(ValkyrienComputersBlocksCC.READER.get());
    }
}
