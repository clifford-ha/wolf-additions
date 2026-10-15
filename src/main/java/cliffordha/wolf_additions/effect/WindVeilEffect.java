package cliffordha.wolf_additions.effect;

import cliffordha.wolf_additions.registry.VWColors;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class WindVeilEffect extends MobEffect {
    public WindVeilEffect() {
        super(MobEffectCategory.BENEFICIAL, VWColors.VERDANT_WIND, new DustParticleOptions(VWColors.VERDANT_WIND, 1.0f));
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
    }
    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
    }
    @Override
    public void onMobRemoved(ServerLevel level, LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
    }
    @Override
    public void onEffectRemoved(MobEffectInstance effectInstance, LivingEntity entity) {
    }
}
