package net.techtastic.vc.fabric.integrations.valkyrienskies.cc;

import dan200.computercraft.api.ComputerCraftAPI;

public class ValkyrienComputersPeripheralProviders {
	public static void registerPeripheralProviders() {
		ComputerCraftAPI.registerPeripheralProvider(new RadarPeripheralProvider());
	}
}
