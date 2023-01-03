package net.techtastic.vc;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.techtastic.vc.registry.DeferredRegister;
import net.techtastic.vc.registry.RegistrySupplier;

public class ValkyrienComputersTab {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.Companion.create(ValkyrienComputersMod.MOD_ID, Registry.ITEM_REGISTRY);

    public static RegistrySupplier<Item> LOGO = ITEMS.register(
            "vc_logo",
            () -> new Item(new Item.Properties())
    );

    public static CreativeModeTab tab;

    public static void registerTab() {
        ITEMS.applyAll();

        tab = CreativeTabRegistry.create(
                new ResourceLocation(ValkyrienComputersMod.MOD_ID, "vc_tab"),
                () -> new ItemStack(LOGO.get())
        );
    }
}
