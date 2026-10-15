package cliffordha.wolf_additions;

import cliffordha.wolf_additions.client.VWTooltips;
import cliffordha.wolf_additions.client.screen.DedicatedConfigScreen;
import cliffordha.wolf_additions.keymapping.VWKeymap;
import cliffordha.wolf_additions.networking.ClientboundPackets;
import cliffordha.wolf_additions.particle.MightParalyzeParticle;
import cliffordha.wolf_additions.particle.VerixiumPowderRainParticle;
import cliffordha.wolf_additions.registry.*;
import cliffordha.wolf_additions.particle.BenedictionTriggerParticle;
import cliffordha.wolf_additions.particle.VerdantBiomesEnvironmentAmbiance;
import cliffordha.wolf_additions.util.VWEffectOverlays;
import cliffordha.wolf_additions.util.VWColorizeTextMixin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

import java.util.List;

public class WolfAdditionsClient implements ClientModInitializer {
    public void onInitializeClient() {
        VWColorizeTextMixin.register(VWColors.VERDANT_WIND, List.of(
                "enchantment.cliffordha-wolf-additions.benediction_of_the_verdant_mountains",

                "item.minecraft.tipped_arrow.effect.sacred_verdant_potion",
                "item.minecraft.potion.effect.sacred_verdant_potion",
                "item.minecraft.splash_potion.effect.sacred_verdant_potion",
                "item.minecraft.lingering_potion.effect.sacred_verdant_potion",

                "effect.cliffordha-wolf-additions.blessing_of_the_verdant_wind",
                "effect.cliffordha-wolf-additions.wind_veil"
                )
        );
        VWColorizeTextMixin.register(VWColors.BLOODLUST_EFFECT, List.of(
                "effect.cliffordha-wolf-additions.getBloodlust"
                )
        );
        VWColorizeTextMixin.register(VWColors.HAVOC_PARTICLE, List.of(
                "effect.cliffordha-wolf-additions.order_of_annihilation"
        ));
        ParticleProviderRegistry.getInstance().register(VWParticles.BENEDICTION_TRIGGER_PARTICLE, BenedictionTriggerParticle.BenedictionParticleProvider::new);
        ParticleProviderRegistry.getInstance().register(VWParticles.VERDANT_BIOMES_ENVIRONMENT_AMBIANCE, VerdantBiomesEnvironmentAmbiance.VerdantBiomesEnvironmentAmbianceProvider::new);
        ParticleProviderRegistry.getInstance().register(VWParticles.VERIXIUM_POWDER_RAIN_PARTICLE, VerixiumPowderRainParticle.VerixiumPowderRainParticleProvider::new);
        ParticleProviderRegistry.getInstance().register(VWParticles.MIGHT_PARALYZE_PARTICLE, MightParalyzeParticle.MightParalyzeParticleProvider::new);

        VWEffectOverlays.register();
        VWTooltips.register();

        ClientboundPackets.registerPackets();
        VWKeymap.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && VWKeymap.isWolfAdditionsConfigPressed()) {
                DedicatedConfigScreen.create();
            }
        });

    }
}
