package cliffordha.wolf_additions.datagen;

import java.util.concurrent.CompletableFuture;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.keymapping.VWKeymap;
import cliffordha.wolf_additions.registry.*;
import net.minecraft.core.HolderLookup;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class VWEngLangProvider extends FabricLanguageProvider {
    public VWEngLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider holderLookup, TranslationBuilder text) {
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_IGNITION, "Wolf ATK Effect: §vIgnition");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_POISONING, "Wolf ATK Effect: §cPoison");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_WITHERING, "Wolf ATK Effect: §cWithering");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_LIFTING, "Wolf ATK Effect: Lifting");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_BLOODLUST, "Wolf ATK Effect: §cBloodlust");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_MIGHT, "Wolf ATK Effect: §dMight");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_OOZING, "Wolf ATK Effect: §aOozing");
        text.addEnchantment(VWEnchantments.WOLF_EFFECT_GNAWING, "Wolf ATK Effect: §dGnawing");
        text.addEnchantment(VWEnchantments.WOLF_ARMOR_ENHANCEMENT_KIT, "Wolf Armor Enhancement Kit");
        text.addEnchantment(VWEnchantments.BENEDICTION_OF_THE_VERDANT_MOUNTAINS,"Benediction of the Verdant Mountains");


        text.add(effectKey("blessing_of_the_verdant_wind"), "Blessing of the Verdant Wind");
        text.add(effectKey("amplified_might"), "Amplified Might");
        text.add(effectKey("bloodlust"), "Bloodlust");
        text.add(effectKey("paralyze"), "Paralyzed");
        text.add(effectKey("wind_veil"), "Wind Veil");
        text.add(effectKey("havoc"), "Havoc");

        text.add("effect.tales-of-the-verdant-wind.bloodlust.description", "Gives massive attack buff in exchange for constant damage while the effect is active");
        
        text.add("item.minecraft.potion.effect.sacred_verdant_potion", "Sacred Verdant Potion");
        text.add("item.minecraft.splash_potion.effect.sacred_verdant_potion", "Sacred Verdant Splash Potion");
        text.add("item.minecraft.lingering_potion.effect.sacred_verdant_potion", "Sacred Verdant Lingering Potion");

        text.add("item.minecraft.potion.effect.might_amplifier_potion", "Amplified Might Potion");
        text.add("item.minecraft.splash_potion.effect.might_amplifier_potion", "Amplified Might Splash Potion");
        text.add("item.minecraft.lingering_potion.effect.might_amplifier_potion", "Amplified Might Lingering Potion");

        text.add("item.minecraft.potion.effect.baleful_strength_potion", "Baleful Strength Potion");
        text.add("item.minecraft.splash_potion.effect.baleful_strength_potion", "Baleful Strength Splash Potion");
        text.add("item.minecraft.lingering_potion.effect.baleful_strength_potion", "Baleful Strength Lingering Potion");

        text.add("item.minecraft.tipped_arrow.effect.sacred_verdant_potion", "Arrow infused with Verdant Wind");
        text.add("item.minecraft.tipped_arrow.effect.might_amplifier_potion", "Arrow of Amplified Might");
        text.add("item.minecraft.tipped_arrow.effect.baleful_strength_potion", "Arrow of Baleful Strength");


        text.add("death.attack.bloodlust", "%1$s died from the agonizing effects of §cBloodlust§r");
        text.add("death.attack.bloodlust.player", "%1$s died from the agonizing effects of §cBloodlust§r while fighting %2$s");

        text.add("death.attack.scorching_heat", "%1$s died from scorching heat");
        text.add("death.attack.scorching_heat.player", "%1$s died from scorching heat while fighting %2$s");

        text.add("death.attack.bleeding", "%1$s bled to death");
        text.add("death.attack.bleeding.player", "%1$s bled to death while fighting %2$s");


        // SOUNDS
        text.add(VWSounds.WOLF_HOWL_A, "Distant wolf howls");
        text.add(VWSounds.WOLF_HOWL_B1, "Distant wolf howls");
        text.add(VWSounds.WOLF_HOWL_B2, "Distant wolf howls");
        text.add(VWSounds.WOLF_HOWL_B3, "Distant wolf howls");

        text.add(VWSounds.NOTIFY, "Notification popped");
        text.add(VWSounds.WOLF_SKILL_PARALYZE, "%1$s got paralyzed");

        text.add(VWSounds.LODESTONE_WIND_CORE_AMBIENT, "Wind Core whooshes");


        text.add(VWKeymap.TOTVW_CONFIG_SCREEN, "Wolf Additions Config Screen");
        text.add(TOTVW.MOD_ID, "Tales of the Verdant Wind");
    }
    private static String effectKey(String name) {
        return "effect." + TOTVW.MOD_ID + "." + name;
    }
}