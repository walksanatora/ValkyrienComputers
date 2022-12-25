package net.techtastic.vc.forge.integrations.cc;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.shared.Registry;
import net.minecraft.world.item.CreativeModeTab;

public class ValkyrienComputersComputerCraftCreativeTab {
    public static CreativeModeTab getComputerCraftTab() {
        return IComputerCraftTabAccess.class.cast(Registry.class).getCreativeTab();
    }
}
