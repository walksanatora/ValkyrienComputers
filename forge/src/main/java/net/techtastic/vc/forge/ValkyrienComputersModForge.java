package net.techtastic.vc.forge;

import net.minecraftforge.fml.loading.LoadingModList;
import net.techtastic.vc.ValkyrienComputersMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.techtastic.vc.forge.integrations.cc.eureka.EurekaPeripheralProviders;
import net.techtastic.vc.forge.integrations.cc.valkyrienskies.ValkyrienComputersPeripheralProviders;
import org.valkyrienskies.core.impl.config.VSConfigClass;
import net.techtastic.vc.ValkyrienComputersConfig;
//import net.techtastic.vc.block.WoodType;
//import net.techtastic.vc.blockentity.renderer.ShipHelmBlockEntityRenderer;
//import net.techtastic.vc.blockentity.renderer.WheelModels;
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig;

@Mod(ValkyrienComputersMod.MOD_ID)
public class ValkyrienComputersModForge {
    boolean happendClientSetup = false;
    static IEventBus MOD_BUS;

    public ValkyrienComputersModForge() {
        // Submit our event bus to let architectury register our content on the right time
        MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MOD_BUS.addListener(this::clientSetup);

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((Minecraft client, Screen parent) ->
                        VSClothConfig.createConfigScreenFor(parent,
                                VSConfigClass.Companion.getRegisteredConfig(ValkyrienComputersConfig.class)))
        );

        MOD_BUS.addListener(this::clientSetup);

        ValkyrienComputersMod.init();

        LoadingModList mods = LoadingModList.get();
        ValkyrienComputersConfig.Server.COMPUTERCRAFT ccConfig = ValkyrienComputersConfig.SERVER.getComputerCraft();
        if (mods.getModFileById("computercraft") != null && !ccConfig.getDisableComputerCraft()) {
            ValkyrienComputersPeripheralProviders.registerPeripheralProviders();

            if (mods.getModFileById("vs_eureka") != null && !ccConfig.getDisableEurekaIntegration()) {
                EurekaPeripheralProviders.registerPeripheralProviders();
            }
        }
    }

    void clientSetup(final FMLClientSetupEvent event) {
        if (happendClientSetup) return;
        happendClientSetup = true;

        ValkyrienComputersMod.initClient();

//        WheelModels.INSTANCE.setModelGetter(woodType -> ForgeModelBakery.instance().getBakedTopLevelModels()
//                .getOrDefault(
//                        new ResourceLocation(ValkyrienComputersMod.MOD_ID, "block/" + woodType.getResourceName() + "_ship_helm_wheel"),
//                        Minecraft.getInstance().getModelManager().getMissingModel()
//                ));
    }

//    void entityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
//        event.registerBlockEntityRenderer(
//                ValkyrienComputersBlockEntities.INSTANCE.getSHIP_HELM().get(),
//                ShipHelmBlockEntityRenderer::new
//        );
//    }

//    void onModelRegistry(final ModelRegistryEvent event) {
//        for (WoodType woodType : WoodType.values()) {
//            ForgeModelBakery.addSpecialModel(new ResourceLocation(ValkyrienComputersMod.MOD_ID, "block/" + woodType.getResourceName() + "_ship_helm_wheel"));
//        }
//    }
}
