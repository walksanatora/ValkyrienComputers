package net.techtastic.vc.tis;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.techtastic.vc.ValkyrienComputersConfig;
import net.techtastic.vc.ValkyrienComputersConfig.Server.TIS;
import net.techtastic.vc.ValkyrienComputersMod;
import net.techtastic.vc.ValkyrienComputersTab;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.registry.RegistrySupplier;

public class ValkyrienComputersItemsTIS {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.Companion.create(ValkyrienComputersMod.MOD_ID, Registry.ITEM_REGISTRY);

    public static RegistrySupplier<Item> RADAR;
    public static RegistrySupplier<Item> READER;

    public static RegistrySupplier<Item> registerItem(String name, Item item) {
        return ITEMS.register(name, () -> item);
    }

    public static void registerItems() {
        CreativeModeTab tab = ValkyrienComputersTab.tab;

        TIS config = ValkyrienComputersConfig.SERVER.getTIS3D();
        if (!config.getDisableRadars()) {
            RADAR = registerItem("radar_tis", new Item(new Item.Properties().tab(tab)));
        }
        if (!config.getDisableShipReaders()) {
            READER = registerItem("reader_tis", new Item(new Item.Properties().tab(tab)));
        }

        ITEMS.applyAll();
    }
}
