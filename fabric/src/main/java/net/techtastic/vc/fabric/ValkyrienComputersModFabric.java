package net.techtastic.vc.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.techtastic.vc.ValkyrienComputersMod;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.techtastic.vc.fabric.integrations.cc.eureka.EurekaPeripheralProviders;
import net.techtastic.vc.fabric.integrations.cc.valkyrienskies.ValkyrienComputersPeripheralProviders;
import org.valkyrienskies.core.impl.config.VSConfigClass;
import net.techtastic.vc.ValkyrienComputersConfig;
//import net.techtastic.vc.block.WoodType;
//import net.techtastic.vc.blockentity.renderer.ShipHelmBlockEntityRenderer;
//import net.techtastic.vc.blockentity.renderer.WheelModels;
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig;
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric;

public class ValkyrienComputersModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // force VS2 to load before vc
        new ValkyrienSkiesModFabric().onInitialize();

        FabricLoader loader = FabricLoader.getInstance();
        ValkyrienComputersConfig.Server.COMPUTERCRAFT ccConfig = ValkyrienComputersConfig.SERVER.getComputerCraft();
        if (loader.isModLoaded("computercraft") && !ccConfig.getDisableComputerCraft()) {
            ValkyrienComputersPeripheralProviders.registerPeripheralProviders();

            if (loader.isModLoaded("vs_eureka") && !ccConfig.getDisableEurekaIntegration()) {
                EurekaPeripheralProviders.registerPeripheralProviders();
            }
        }

        ValkyrienComputersMod.init();
    }

    @Environment(EnvType.CLIENT)
    public static class Client implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            ValkyrienComputersMod.initClient();
//            BlockEntityRendererRegistry.INSTANCE.register(
//                    ValkyrienComputersBlockEntities.INSTANCE.getSHIP_HELM().get(),
//                    ShipHelmBlockEntityRenderer::new
//            );

//            ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
//                for (WoodType woodType : WoodType.values()) {
//                    out.accept(new ResourceLocation(ValkyrienComputersMod.MOD_ID, "block/" + woodType.getResourceName() + "_ship_helm_wheel"));
//                }
//            });
//
//            WheelModels.INSTANCE.setModelGetter(woodType ->
//                    Minecraft.getInstance().getModelManager().getModel(
//                            new ModelResourceLocation(
//                                    new ResourceLocation(ValkyrienComputersMod.MOD_ID, "ship_helm_wheel"),
//                                    "wood=" + woodType.getResourceName()
//                            )));
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
