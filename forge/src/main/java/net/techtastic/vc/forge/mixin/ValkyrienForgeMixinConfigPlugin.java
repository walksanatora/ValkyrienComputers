package net.techtastic.vc.forge.mixin;

import net.techtastic.vc.forge.AutoDependenciesForge;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * For now, just using this class as an abusive early entrypoint to run the updater
 */
public class ValkyrienForgeMixinConfigPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(final String s) {
        System.err.println("Called onLoad");
        AutoDependenciesForge.runUpdater();
    }

    @Override
    public String getRefMapperConfig() {
        System.err.println("Called getRefMapperConfig");
        return null;
    }

    @Override
    public boolean shouldApplyMixin(final String s, final String s1) {
        System.err.println("Called shouldApplyMixin");
        return true;
    }

    @Override
    public void acceptTargets(final Set<String> set, final Set<String> set1) {
        System.err.println("Called acceptTargets");
    }

    @Override
    public List<String> getMixins() {
        System.err.println("Called getMixins");
        return null;
    }

    @Override
    public void preApply(final String s, final ClassNode classNode, final String s1, final IMixinInfo iMixinInfo) {
        System.err.println("Called preApply");
    }

    @Override
    public void postApply(final String s, final ClassNode classNode, final String s1, final IMixinInfo iMixinInfo) {
        System.err.println("Called postApply");
    }
}
