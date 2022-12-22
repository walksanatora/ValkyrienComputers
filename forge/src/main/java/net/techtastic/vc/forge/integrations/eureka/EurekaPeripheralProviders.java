package net.techtastic.vc.forge.integrations.eureka;

import dan200.computercraft.api.ComputerCraftAPI;

public class EurekaPeripheralProviders {
    public static void registerPeripheralProviders() {
        ComputerCraftAPI.registerPeripheralProvider(new ShipHelmPeripheralProvider());
    }
}
