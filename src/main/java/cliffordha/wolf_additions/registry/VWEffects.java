package cliffordha.wolf_additions.registry;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.effect.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

import static cliffordha.wolf_additions.TOTVW.sendClassRegisterLog;

public class VWEffects {
    public static final Holder<MobEffect> AMPLIFIED_MIGHT = registerMobEffect("amplified_might",
            new AmplifiedMightEffect());

    public static final Holder<MobEffect> BLESSING_OF_THE_VERDANT_WIND = registerMobEffect("blessing_of_the_verdant_wind",
            new BlessingOfTheVerdantWindEffect());

    public static final Holder<MobEffect> BLOODLUST = registerMobEffect("bloodlust",
            new BloodlustEffect());

    public static final Holder<MobEffect> PARALYZE = registerMobEffect("paralyze",
            new ParalyzeEffect());

    public static final Holder<MobEffect> WIND_VEIL = registerMobEffect("wind_veil",
            new WindVeilEffect());

    private static Holder<MobEffect> registerMobEffect(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                TOTVW.registerID(name), effect);
    }

    public static void addModifier(AttributeMap map, Identifier id, Holder<Attribute> name, double value, AttributeModifier.Operation operation) {
        if (map.hasAttribute(name)) {
            map.getInstance(name).addOrReplacePermanentModifier(new AttributeModifier(id, value, operation));
        }
    }
    public static void addMultipleModifier(AttributeMap map, Identifier id, double value, AttributeModifier.Operation operation, List<Holder<Attribute>> list) {
        for (Holder<Attribute> name : list) {
            if (map.hasAttribute(name)) {
                map.getInstance(name).addOrReplacePermanentModifier(new AttributeModifier(id, value, operation));
            }
        }
    }

    public static void removeAllModifiers(LivingEntity entity, Identifier id, List<Holder<Attribute>> list) {
        AttributeMap attributes = entity.getAttributes();
        for (Holder<Attribute> m : list) {
            if (attributes.hasAttribute(m) && attributes.hasModifier(m, id)) {
                entity.getAttribute(m).removeModifier(id);
            }
        }
    }

    public static void register() {
        sendClassRegisterLog("Effects");
    }
}