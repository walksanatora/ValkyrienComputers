package net.techtastic.vc.integrations.cc.eureka;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.eureka.block.ShipHelmBlock;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.mod.api.SeatedControllingPlayer;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.List;
import java.util.Optional;

public class ShipHelmPeripheral implements IPeripheral {
    private final Level world;
    private final BlockPos pos;

    public ShipHelmPeripheral(Level level, BlockPos blockPos) {
        this.world = level;
        this.pos = blockPos;
    }

    @NotNull
    @Override
    public String getType() {
        return "ship_helm";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        if (world != null) {
            return world.getBlockState(pos).getBlock() instanceof ShipHelmBlock;
        }
        return false;
    }

    @LuaFunction
    public final void move(IArguments arg) throws LuaException {
        Optional<String> direction = arg.optString(0);
        boolean bool = arg.optBoolean(0, false);

        if (direction.isEmpty()) throw new LuaException("missing direction");

        if (world.isClientSide) return;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        if (fakePlayer == null) {
            fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
            ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);
        }


        switch (direction.get()) {
            case "forward" -> fakePlayer.setForwardImpulse(bool ? 1.0f : 0.0f);
            case "back" -> fakePlayer.setForwardImpulse(bool ? -1.0f : 0.0f);
            case "left" -> fakePlayer.setLeftImpulse(bool ? 1.0f : 0.0f);
            case "right" -> fakePlayer.setLeftImpulse(bool ? -1.0f : 0.0f);
            case "up" -> fakePlayer.setUpImpulse(bool ? 1.0f : 0.0f);
            case "down" -> fakePlayer.setUpImpulse(bool ? -1.0f : 0.0f);
            default -> throw new LuaException("invalid direction");
        }
    }

    @LuaFunction
    public final void resetAllMovement() throws LuaException {
        if (world.isClientSide) return;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        if (fakePlayer == null) {
            fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
            ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);
        }

        fakePlayer.setForwardImpulse(0.0f);
        fakePlayer.setLeftImpulse(0.0f);
        fakePlayer.setUpImpulse(0.0f);
    }

    @LuaFunction
    public final boolean isCruising() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        return fakePlayer != null && fakePlayer.getCruise();
    }

    @LuaFunction
    public final boolean startCruising() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        if (fakePlayer == null) {
            fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
        }

        fakePlayer.setCruise(true);
        ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);

        return true;
    }

    @LuaFunction
    public final boolean stopCruising() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        if (fakePlayer == null) {
            fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
        }

        fakePlayer.setCruise(false);
        ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);

        return true;
    }

    @LuaFunction
    public final boolean startAlignment() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        control.setAligning(true);
        ship.saveAttachment(EurekaShipControl.class, control);

        return true;
    }

    @LuaFunction
    public final boolean stopAlignment() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        control.setAligning(false);
        ship.saveAttachment(EurekaShipControl.class, control);

        return true;
    }

    @LuaFunction
    public final boolean disassemble() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ShipHelmBlockEntity helm) {
            helm.disassemble();

            return true;
        } else {
            throw new LuaException("no ship helm");
        }
    }

    @LuaFunction
    public final boolean assemble() throws LuaException {
        if (world.isClientSide()) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) throw new LuaException("already assembled");

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ShipHelmBlockEntity helm) {
            helm.assemble();

            return true;
        } else {
            throw new LuaException("no ship helm");
        }
    }

    @LuaFunction
    public final int getBalloonAmount() throws LuaException {
        if (world.isClientSide) return 0;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        return control.getBalloons();
    }

    @LuaFunction
    public final int getAnchorAmount() throws LuaException {
        if (world.isClientSide) return 0;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        return control.getAnchors();
    }

    @LuaFunction
    public final int getActiveAnchorAmount() throws LuaException {
        if (world.isClientSide) return 0;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        return  control.getAnchorsActive();
    }

    @LuaFunction
    public final boolean areAnchorsActive() throws LuaException {
        if (world.isClientSide) return false;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        return control.getAnchorsActive() > 0;
    }

    @LuaFunction
    public final int getShipHelmAmount() throws LuaException {
        if (world.isClientSide) return 0;
        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        return control.getHelms();
    }
}
