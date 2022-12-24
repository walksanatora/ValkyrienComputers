package net.techtastic.vc.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.techtastic.vc.ValkyrienComputersConfig;
import org.valkyrienskies.core.config.VSConfigClass;
import org.valkyrienskies.mod.compat.clothconfig.VSClothConfig;

public class ValkyrienComputersModMenuFabric implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> VSClothConfig.createConfigScreenFor(
                parent,
                VSConfigClass.Companion.getRegisteredConfig(ValkyrienComputersConfig.class)
        );
    }
}
