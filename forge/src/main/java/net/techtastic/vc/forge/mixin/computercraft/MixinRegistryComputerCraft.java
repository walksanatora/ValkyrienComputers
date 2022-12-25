package net.techtastic.vc.forge.mixin.computercraft;

import dan200.computercraft.shared.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.techtastic.vc.forge.integrations.cc.IComputerCraftTabAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(Registry.class)
public class MixinRegistryComputerCraft implements IComputerCraftTabAccess {
    @Shadow @Final private static CreativeModeTab mainItemGroup;

    @Override
    public CreativeModeTab getCreativeTab() {
        return mainItemGroup;
    }
}
