package net.techtastic.vc.forge.integrations.cc.eureka;

import dan200.computercraft.api.ComputerCraftAPI;

public class EurekaPeripheralProviders {
    public static void registerPeripheralProviders() {
        ComputerCraftAPI.registerPeripheralProvider(new ShipHelmPeripheralProvider());
    }
}
