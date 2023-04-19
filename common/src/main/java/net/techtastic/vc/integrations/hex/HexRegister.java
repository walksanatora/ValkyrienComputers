import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.misc.ManaConstants;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import net.techtastic.vc.integrations.hex.ops.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class HexRegister {
    public static void registerPatterns() {
        try {
            //HexPattern(NORTH_EAST awqawdwwedaqedwadweqewwd), greater amogus
            PatternRegistry.mapPattern(
                    HexPattern.fromAngles(
                            "awqawdwwedaqedwadweqewwd", HexDir.NORTH_EAST
                    ),
                    new ResourceLocation("ritualhex", "amogus"),
                    OpAmogus.INSTANCE, true
            );
        } catch (PatternRegistry.RegisterPatternException exn) {
            exn.printStackTrace();
        }
    }
}