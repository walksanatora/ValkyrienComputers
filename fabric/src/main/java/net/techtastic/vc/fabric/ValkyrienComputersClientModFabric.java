package net.techtastic.vc.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.techtastic.vc.ValkyrienComputersMod;

public class ValkyrienComputersClientModFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ValkyrienComputersMod.initClient();
    }
}
