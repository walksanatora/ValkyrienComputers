package net.techtastic.vc.forge.integrations.cc;

import net.minecraft.world.item.CreativeModeTab;

public interface IComputerCraftTabAccess {
    default CreativeModeTab getCreativeTab() {
        return null;
    }
}
