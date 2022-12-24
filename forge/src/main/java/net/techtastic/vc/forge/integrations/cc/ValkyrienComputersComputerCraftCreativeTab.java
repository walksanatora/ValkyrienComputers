package net.techtastic.vc.forge.integrations.cc;

import dan200.computercraft.shared.Registry;
import net.minecraft.world.item.CreativeModeTab;

public class ValkyrienComputersComputerCraftCreativeTab {
    public static CreativeModeTab getComputerCraftTab() {
        if (Registry.ModItems.DISK.isPresent()) {
            return Registry.ModItems.DISK.get().getItemCategory();
        }

        return CreativeModeTab.TAB_SEARCH;
    }
}
