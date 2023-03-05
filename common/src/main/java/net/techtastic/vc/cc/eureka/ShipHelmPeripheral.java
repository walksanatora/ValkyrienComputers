package net.techtastic.vc.cc.eureka;

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
import org.valkyrienskies.core.game.ships.ShipData;
import org.valkyrienskies.eureka.block.ShipHelmBlock;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.mod.api.SeatedControllingPlayer;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.util.Optional;

public class ShipHelmPeripheral implements IPeripheral {
    private Level world;
    private BlockPos pos;

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

        if (!direction.isPresent()) throw new LuaException("missing direction");

        if (world.isClientSide) return;
        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) throw new LuaException("no ship");
        EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
        if (control == null) throw new LuaException("not Eureka ship");

        SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
        if (fakePlayer == null) {
            fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
            ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);
        }


        switch (direction.get()) {
            case "forward": fakePlayer.setForwardImpulse(bool ? 1.0f : 0.0f); break;
            case "back": fakePlayer.setForwardImpulse(bool ? -1.0f : 0.0f); break;
            case "left": fakePlayer.setLeftImpulse(bool ? 1.0f : 0.0f); break;
            case "right": fakePlayer.setLeftImpulse(bool ? -1.0f : 0.0f); break;
            case "up": fakePlayer.setUpImpulse(bool ? 1.0f : 0.0f); break;
            case "down": fakePlayer.setUpImpulse(bool ? -1.0f : 0.0f); break;
            default: throw new LuaException("invalid direction");
        }
    }

    @LuaFunction
    public final void resetAllMovement() throws LuaException {
        if (world.isClientSide) return;
        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
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
        if (world.isClientSide()) {
            ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
            if (ship != null) {
                EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
                if (control != null) {
                    SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
                    return fakePlayer != null && fakePlayer.getCruise();
                } else {
                    throw new LuaException("not Eureka ship");
                }
            } else {
                throw new LuaException("no ship");
            }
        }
        return false;
    }

    @LuaFunction
    public final boolean startCruising() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
                if (fakePlayer == null) {
                    fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
                }

                fakePlayer.setCruise(true);
                ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);

                return true;
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final boolean stopCruising() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
                if (fakePlayer == null) {
                    fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
                }

                fakePlayer.setCruise(false);
                ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);

                return true;
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final boolean startAlignment() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                control.setAligning(true);
                ship.saveAttachment(EurekaShipControl.class, control);

                return true;
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final boolean stopAlignment() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                control.setAligning(false);
                ship.saveAttachment(EurekaShipControl.class, control);

                return true;
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final boolean disassemble() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof ShipHelmBlockEntity) {
                    ShipHelmBlockEntity helm = (ShipHelmBlockEntity) be;
                    helm.disassemble();

                    return true;
                } else {
                    throw new LuaException("no ship helm");
                }
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final boolean assemble() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship == null) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ShipHelmBlockEntity) {
                ShipHelmBlockEntity helm = (ShipHelmBlockEntity) be;
                helm.assemble();

                return true;
            } else {
                throw new LuaException("no ship helm");
            }
        } else {
            throw new LuaException("already assembled");
        }
    }

    @LuaFunction
    public final int getBalloonAmount() throws LuaException {
        if (world.isClientSide()) return 0;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                return control.getBalloons();
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final int getAnchorAmount() throws LuaException {
        if (world.isClientSide()) return 0;
        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                return control.getAnchors();
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final int getActiveAnchorAmount() throws LuaException {
        if (world.isClientSide()) return 0;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                return control.getAnchorsActive();
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final boolean areAnchorsActive() throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                return control.getAnchorsActive() > 0;
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    @LuaFunction
    public final int getShipHelmAmount() throws LuaException {
        if (world.isClientSide()) return 0;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) {
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) {
                return control.getHelms();
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }

    public boolean applyThrust(String direction, int gameTicks) throws LuaException {
        if (world.isClientSide()) return false;

        ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) world, pos);
        if (ship != null) { //Is The Peripheral on a Ship?
            EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
            if (control != null) { //Is the Ship being controlled by Eureka?
                SeatedControllingPlayer fakePlayer = ship.getAttachment(SeatedControllingPlayer.class);
                if (fakePlayer == null) { //Is there a SeatedControllingPlayer already?
                    fakePlayer = new SeatedControllingPlayer(world.getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
                    ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);
                }

                long originTime = world.getGameTime();
                int ticks = 0;
                while (ticks < gameTicks) {
                    //If one tick of time has passed, set originTime to current and increment ticks
                    if (world.getGameTime() - originTime == 1) {
                        originTime = world.getGameTime();
                        ticks++;
                    }

                    switch (direction) {
                        case "forward": //Move Ship Forward
                            fakePlayer.setForwardImpulse(1.0f);
                            break;
                        case "left": //Turn Ship Left
                            fakePlayer.setLeftImpulse(1.0f);
                            break;
                        case "right": //Turn Ship Right
                            fakePlayer.setLeftImpulse(-1.0f);
                            break;
                        case "back": //Move Ship Backward
                            fakePlayer.setForwardImpulse(-1.0f);
                            break;
                        case "up": //Move Ship Upward
                            fakePlayer.setUpImpulse(1.0f);
                            break;
                        default: //Move Ship Downward
                            fakePlayer.setUpImpulse(-1.0f);
                            break;
                    }

                    ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);
                }

                switch (direction) {
                    case "forward":
                    case "back": //Reset Forward/Backward Impulse
                        fakePlayer.setForwardImpulse(0.0f);
                        break;
                    case "left":
                    case "right": //Reset Left/Right Impulse
                        fakePlayer.setLeftImpulse(0.0f);
                        break;
                    default: //Reset Up/Down Impulse
                        fakePlayer.setUpImpulse(0.0f);
                        break;
                }

                ship.saveAttachment(SeatedControllingPlayer.class, fakePlayer);

                return true;
            } else {
                throw new LuaException("not Eureka ship");
            }
        } else {
            throw new LuaException("no ship");
        }
    }
}
