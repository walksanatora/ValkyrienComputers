package net.techtastic.vc.fabric.integrations.tis.eureka;

import li.cil.tis3d.common.API;

public class EurekaSerialInterfaceProviders {
	public static void registerSerialInterfaceProivders() {
		API.serial.addProvider(new ShipHelmSerialInterfaceProvider());
	}
}
