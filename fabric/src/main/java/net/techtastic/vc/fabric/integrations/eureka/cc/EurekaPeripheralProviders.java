package net.techtastic.vc.fabric.integrations.eureka.cc;

import dan200.computercraft.api.ComputerCraftAPI;
import net.techtastic.vc.integrations.eureka.cc.ShipHelmPeripheralProvider;

public class EurekaPeripheralProviders {
    public static void registerPeripheralProviders() {
        ComputerCraftAPI.registerPeripheralProvider(new ShipHelmPeripheralProvider());
    }
}
