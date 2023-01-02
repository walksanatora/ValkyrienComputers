package net.techtastic.vc.fabric.integrations.tis.eureka;

import li.cil.tis3d.api.serial.SerialInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.valkyrienskies.core.game.ships.ShipData;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.mod.api.SeatedControllingPlayer;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class ShipHelmSerialInterface implements SerialInterface {
	private ShipHelmBlockEntity helm;
	private Info info;
	private Impulses impulse;

	public ShipHelmSerialInterface(ShipHelmBlockEntity helm) {
		this.helm = helm;
		this.info = Info.BALLOONS;
		this.impulse = Impulses.NONE;
	}

	private enum Info {
		BALLOONS,
		ANCHORS,
		ACTIVE_ANCHORS,
		HELMS,
		CRUISING,
		ALIGNING
	}

	private enum Impulses {
		FORWARD,
		BACKWARD,
		LEFT,
		RIGHT,
		UP,
		DOWN,
		CRUISE,
		ALIGN,
		NONE
	}

	@Override
	public boolean canWrite() {
		return true;
	}

	@Override
	public void write(short i) {
		switch (i) {
			case 0:
				info = Info.HELMS;
				return;
			case 1:
				info = Info.ANCHORS;
				return;
			case 2:
				info = Info.BALLOONS;
				return;
			case 3:
				info = Info.ACTIVE_ANCHORS;
				return;
			case 4:
				info = Info.CRUISING;
				return;
			case 5:
				info = Info.ALIGNING;
				return;
			case 6:
				impulse = Impulses.FORWARD;
				break;
			case 7:
				impulse = Impulses.BACKWARD;
				break;
			case 8:
				impulse = Impulses.LEFT;
				break;
			case 9:
				impulse = Impulses.RIGHT;
				break;
			case 10:
				impulse = Impulses.UP;
				break;
			case 11:
				impulse = Impulses.DOWN;
				break;
			case 12:
				impulse = Impulses.CRUISE;
				break;
			case 13:
				impulse = Impulses.ALIGN;
				break;
			case 14:
				helm.assemble();
				break;
			case 15:
				helm.disassemble();
				break;
			default:
				return;
		}

		ShipData data = VSGameUtilsKt.getShipManagingPos((ServerLevel) helm.getLevel(), helm.getBlockPos());
		if (data == null) return;
		EurekaShipControl control = data.getAttachment(EurekaShipControl.class);
		if (control == null) return;
		SeatedControllingPlayer player = data.getAttachment(SeatedControllingPlayer.class);
		if (player == null) {
			player = new SeatedControllingPlayer(helm.getBlockState().getValue(BlockStateProperties.FACING));
			data.saveAttachment(SeatedControllingPlayer.class, player);
		}
		long currentTime = helm.getLevel().getGameTime();
		while (helm.getLevel().getGameTime() - currentTime < 1) {
			switch (impulse) {
				case FORWARD:
					player.setForwardImpulse(1.0f);
					break;
				case BACKWARD:
					player.setForwardImpulse(-1.0f);
				case LEFT:
					player.setLeftImpulse(1.0f);
					break;
				case RIGHT:
					player.setLeftImpulse(-1.0f);
					break;
				case UP:
					player.setUpImpulse(1.0f);
					break;
				case DOWN:
					player.setUpImpulse(-1.0f);
					break;
				case CRUISE:
					player.setCruise(!player.getCruise());
					data.saveAttachment(SeatedControllingPlayer.class, player);
					return;
				case ALIGN:
					control.setAligning(!control.getAligning());
					data.saveAttachment(EurekaShipControl.class, control);
					return;
				default:
					break;
			}
			data.saveAttachment(SeatedControllingPlayer.class, player);
		}

		switch (impulse) {
			case FORWARD:
				player.setForwardImpulse(0.0f);
				break;
			case BACKWARD:
				player.setForwardImpulse(0.0f);
			case LEFT:
				player.setLeftImpulse(0.0f);
				break;
			case RIGHT:
				player.setLeftImpulse(0.0f);
				break;
			case UP:
				player.setUpImpulse(0.0f);
				break;
			case DOWN:
				player.setUpImpulse(0.0f);
				break;
			default:
				return;
		}

		impulse = Impulses.NONE;
		data.saveAttachment(SeatedControllingPlayer.class, player);
	}

	@Override
	public boolean canRead() {
		return true;
	}

	@Override
	public short peek() {
		ShipData ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) helm.getLevel(), helm.getBlockPos());
		if (ship == null) return (short) 0xFFFF;
		EurekaShipControl control = ship.getAttachment(EurekaShipControl.class);
		if (control == null) return (short) 0xFFFF;
		SeatedControllingPlayer player = ship.getAttachment(SeatedControllingPlayer.class);

		switch (info) {
			case HELMS:
				return (short) control.getHelms();
			case ANCHORS:
				return (short) control.getAnchors();
			case BALLOONS:
				return (short) control.getBalloons();
			case ACTIVE_ANCHORS:
				return (short) control.getAnchorsActive();
			case CRUISING:
				if (player == null) return (short) 0xFFFF;
				if (player.getCruise()) return 1;
				return 0;
			case ALIGNING:
				if (control.getAligning()) return 1;
				return 0;
			default:
				return (short) 0xFFFF;
		}
	}

	@Override
	public void skip() {
	}

	@Override
	public void reset() {
	}

	@Override
	public void writeToNBT(CompoundTag compoundTag) {
	}

	@Override
	public void readFromNBT(CompoundTag compoundTag) {
	}
}
