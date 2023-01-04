package net.techtastic.vc.forge.cc.eureka;

import dan200.computercraft.api.ComputerCraftAPI;

public class EurekaPeripheralProviders {
    public static void registerPeripheralProviders() {
        ComputerCraftAPI.registerPeripheralProvider(new ShipHelmFabricPeripheralProvider());
    }
}
