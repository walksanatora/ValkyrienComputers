package net.techtastic.vc.fabric.mixin.client.main;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.techtastic.vc.fabric.AutoDependenciesFabric;

@Mixin(Main.class)
public class MixinMain {

    @Inject(
            at = @At("HEAD"),
            method = "main",
            remap = false
    )
    private static void beforeMain(final String[] args, final CallbackInfo ci) {
        AutoDependenciesFabric.runUpdater();
    }

}
