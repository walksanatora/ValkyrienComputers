package net.techtastic.vc.fabric.integrations.tis.eureka;

import li.cil.tis3d.api.serial.SerialInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import org.valkyrienskies.core.game.ships.ShipData;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;
import org.valkyrienskies.eureka.ship.EurekaShipControl;
import org.valkyrienskies.mod.api.SeatedControllingPlayer;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class ShipHelmSerialInterface implements SerialInterface {
	private ShipHelmBlockEntity helm;
	private Info info;

	public ShipHelmSerialInterface(ShipHelmBlockEntity helm) {
		this.helm = helm;
		this.info = Info.BALLOONS;
	}

	private enum Info {
		BALLOONS,
		ANCHORS,
		ACTIVE_ANCHORS,
		HELMS,
		CRUISING,
		ALIGNING
	}

	@Override
	public boolean canWrite() {
		return true;
	}

	@Override
	public void write(short i) {
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
