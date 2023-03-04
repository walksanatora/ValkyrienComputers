package net.techtastic.vc.integrations.cc.valkyrienskies;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.integrations.cc.ComputerCraftBlocks;
import net.techtastic.vc.util.SpecialLuaTables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.HashMap;

public class ShipReaderPeripheral implements IPeripheral {

    private Level level;
    private BlockPos pos;

    public ShipReaderPeripheral(Level level, BlockPos worldPosition) {
        this.level = level;
        this.pos = worldPosition;
    }

    /*@LuaFunction
    public final String getShipName() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return ship.getName();
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final boolean setShipName(String string) throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            ship.setName(string);
            return true;
        } else {
            throw new LuaException("Not on a Ship");
        }
    }*/

    @LuaFunction
    public final long getShipID() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return ship.getId();
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final double getMass() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return ship.getInertiaData().getMass();
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getVelocity() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getVelocity());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getWorldspacePosition() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getTransform().getPositionInWorld());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getShipyardPosition() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getTransform().getPositionInShip());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<Object, Object> getScale() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            return SpecialLuaTables.getVectorAsTable(ship.getTransform().getShipToWorldScaling());
        } else {
            throw new LuaException("Not on a Ship");
        }
    }

    @LuaFunction
    public final HashMap<String, Integer> getSize() throws LuaException {
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            AABBic aabb = ship.getShipAABB();
            HashMap<String, Integer> map = new HashMap<>();
            assert aabb != null;
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
        ServerShip ship = isEnabledAndOnShip(pos);
        if (ship != null) {
            if (isQuaternion) {
                return SpecialLuaTables.getQuaternionAsTable(ship.getTransform().getShipToWorldRotation());
            } else {
                HashMap<Object, Object> rpy = new HashMap<>();
                Quaterniondc quat = ship.getTransform().getShipToWorldRotation();
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
        ServerShip ship = isEnabledAndOnShip(new BlockPos(x, y, z));
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
        ServerShip ship = isEnabledAndOnShip(new BlockPos(x, y, z));
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

    public ServerShip isEnabledAndOnShip(BlockPos position) throws LuaException {
        if (level.isClientSide()) return null;
        if (ValkyrienComputersConfig.SERVER.getComputerCraft().getDisableShipReaders()) throw new LuaException("Disabled");
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, position);
        return ship;
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
