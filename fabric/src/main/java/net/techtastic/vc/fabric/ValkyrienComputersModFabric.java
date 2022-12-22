package net.techtastic.vc.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.ValkyrienComputersConfig.Server.COMPUTERCRAFT;
import net.techtastic.vc.ValkyrienComputersConfig.Server.OPENCOMPUTERS;
import net.techtastic.vc.ValkyrienComputersMod;
import net.techtastic.vc.fabric.integrations.eureka.cc.EurekaPeripheralProviders;
import net.techtastic.vc.fabric.integrations.valkyrienskies.cc.ValkyrienComputersPeripheralProviders;
import org.valkyrienskies.core.config.VSConfigClass;
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig;

public class ValkyrienComputersModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        try {
            Class.forName("org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric");
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ValkyrienComputersMod.init();

        FabricLoader mods = FabricLoader.getInstance();
        COMPUTERCRAFT CC_Config = ValkyrienComputersConfig.SERVER.getComputerCraft();
        if (mods.isModLoaded("computercraft") && !CC_Config.getDisableComputerCraft()) {
            // ComputerCraft is loaded and Integration is not disabled in the config

            ValkyrienComputersBlocksCC.registerCCBlocks();
            ValkyrienComputersPeripheralProviders.registerPeripheralProviders();

            if (mods.isModLoaded("vs_eureka")) {
                // Eureka is loaded
                EurekaPeripheralProviders.registerPeripheralProviders();
            }
        }

        OPENCOMPUTERS OC_Config = ValkyrienComputersConfig.SERVER.getOpenComputers();
        if (mods.isModLoaded("opencomputers") && !OC_Config.getDisableOpenComputers()) {
            // ComputerCraft is loaded and Integration is not disabled in the config
            if (mods.isModLoaded("vs_eureka")) {
                // Eureka is loaded
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Client implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            ValkyrienComputersMod.initClient();
        }
    }

    public static class ModMenu implements ModMenuApi {
        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory() {
            return (parent) -> VSClothConfig.createConfigScreenFor(
                    parent,
                    VSConfigClass.Companion.getRegisteredConfig(ValkyrienComputersConfig.class)
            );
        }
    }
}
