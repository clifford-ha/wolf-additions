package cliffordha.wolf_additions.registry;

import cliffordha.wolf_additions.TOTVW;
import net.minecraft.resources.Identifier;

public class VWIdentifiers {
    public static final Identifier EFFECT_AMPLIFIED_MIGHT = register("effect_amplified_might");
    public static final Identifier EFFECT_BLESSING_OF_THE_VERDANT_WIND = register("effect_blessing_of_the_verdant_wind");
    public static final Identifier EFFECT_BLOODLUST = register("effect_bloodlust");
    public static final Identifier EFFECT_BLOODLUST_ADDITIONAL = register("effect_bloodlust_additional");
    public static final Identifier EFFECT_PARALYZE = register("effect_paralyze");

    public static final Identifier VERDANT_WOLF_PERMANENT_MODIFIERS = register("verdant_wolf_permanent_modifiers");

    private static Identifier register(String name) {
        return TOTVW.registerID(name);
    }
}
