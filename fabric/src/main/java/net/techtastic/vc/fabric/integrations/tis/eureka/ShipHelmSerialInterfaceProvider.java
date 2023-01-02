package net.techtastic.vc.fabric.integrations.tis.eureka;

import li.cil.tis3d.api.serial.SerialInterface;
import li.cil.tis3d.api.serial.SerialInterfaceProvider;
import li.cil.tis3d.api.serial.SerialProtocolDocumentationReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.valkyrienskies.eureka.blockentity.ShipHelmBlockEntity;

public class ShipHelmSerialInterfaceProvider implements SerialInterfaceProvider {
	@Override
	public boolean worksWith(final Level world, final BlockPos position, final Direction side) {
		return world.getBlockEntity(position) instanceof ShipHelmBlockEntity;
	}

	@Override
	public SerialInterface interfaceFor(final Level world, final BlockPos position, final Direction side) {
		final ShipHelmBlockEntity helm = (ShipHelmBlockEntity) world.getBlockEntity(position);
		if (helm == null) {
			throw new IllegalArgumentException("Provided location does not contain a Ship Helm. Check via worksWith first.");
		}
		return new ShipHelmSerialInterface(helm);
	}

	@Override
	public SerialProtocolDocumentationReference getDocumentationReference() {
		return new SerialProtocolDocumentationReference("Eureka Ship Helm", "additions/protocols/eureka_ship_helm.md");
	}

	@Override
	public boolean isValid(final Level world, final BlockPos position, final Direction side, final SerialInterface serialInterface) {
		return serialInterface instanceof ShipHelmSerialInterface;
	}
}
