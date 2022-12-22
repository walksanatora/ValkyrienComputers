package net.techtastic.vc.forge;

import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.valkyrienskies.dependency_downloader.ValkyrienDependencyDownloader;

public class AutoDependenciesForge {
    public static void runUpdater() {
        boolean isServer = FMLEnvironment.dist.isDedicatedServer();

        ValkyrienDependencyDownloader.start(
            FMLPaths.MODSDIR.get(),
            FMLLoader.getLoadingModList().getModFileById("vc").getFile().getFilePath(),
            isServer
        );
    }
}
