package cliffordha.wolf_additions.entity;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.entity.player.PlayerAtrocityCounter;
import cliffordha.wolf_additions.entity.skills.RevivalByProxy;
import cliffordha.wolf_additions.entity.player.VWPlayerBehaviors;
import cliffordha.wolf_additions.entity.skills.VerdantWindBlessing;
import cliffordha.wolf_additions.entity.wolf.VWWolfBehaviors;
import cliffordha.wolf_additions.registry.*;
import cliffordha.wolf_additions.registry.attachments.VWAttachments;
import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import cliffordha.wolf_additions.registry.attachments.entity.WolfAttachment;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cliffordha.wolf_additions.TOTVW.sendClassRegisterLog;
import static cliffordha.wolf_additions.util.VWUtil.*;

public class VWGlobalEntityBehaviors {
    public static void register() {
        onDamageOrDeathEvent();

        if (TOTVW.IN_DEVELOPMENT) {
            developmentTick();
        }

        VWPlayerBehaviors.registerModPlayerBehaviors();
        VWWolfBehaviors.registerModWolfBehaviors();
        sendClassRegisterLog("Custom Entity Behaviors");
    }


    private static void developmentTick() {
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            for (var serverLevel : server.getAllLevels()) {
                serverLevel.getEntities(EntityTypes.PLAYER, _ -> true).forEach(player -> {
                    if (!player.entityTags().contains(player.getStringUUID() + "-reminderStamp")) {
                        sendToChat(player, VWColors.VERDANT_WIND, false, "TOTVW mod version is a development build.");
                        player.entityTags().add(player.getStringUUID() + "-reminderStamp");
                    }
                    if (!player.getAttachedOrElse(PlayerAttachment.IS_DEV_MODE, false)) {
                        player.setAttached(PlayerAttachment.IS_DEV_MODE, true);
                    }
                });
            }
        });
    }

    private static void onDamageOrDeathEvent() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(
                VWGlobalEntityBehaviors::allowDamageEvent
        );
        ServerLivingEntityEvents.AFTER_DAMAGE.register(
                (victim, damageSource, _, damage, _) -> afterDamageEvent(victim, damageSource, damage)
        );
        ServerLivingEntityEvents.AFTER_DEATH.register(
                VWGlobalEntityBehaviors::afterDeathEvent
        );
        ServerLivingEntityEvents.ALLOW_DEATH.register(
                RevivalByProxy::revivePlayerIfPossible
        );
    }
    private static void afterDeathEvent(LivingEntity entity, DamageSource damageSource) {
        Entity attacker = damageSource.getEntity();

        if (attacker instanceof Player player) {
            if (entity.is(EntityTypes.WOLF) || entity.is(EntityTypes.VILLAGER) || entity.is(EntityTypes.WANDERING_TRADER)) {
                PlayerAtrocityCounter.processAtrocity(player, entity, true);
            }
        }
        if (entity instanceof Player player) {
            if (attacker instanceof Player attackerPlayer) {
                updateNearbyWolvesOnDamage(player, attackerPlayer);
            }
        }
    }
    private static void afterDamageEvent(LivingEntity victim, DamageSource damageSource, float dmg) {
        if (victim == null) return;
        Entity attacker = damageSource.getEntity();

        if (attacker instanceof Player player) {
            if (victim.is(EntityTypes.WOLF) || victim.is(EntityTypes.VILLAGER) || victim.is(EntityTypes.WANDERING_TRADER)) {
                PlayerAtrocityCounter.processAtrocity(player, victim, false);
            }
        }
        if (victim instanceof Player player) {
            if (attacker instanceof Player attackerPlayer) {
                updateNearbyWolvesOnDamage(player, attackerPlayer);
            }
        }
    }
    private static boolean allowDamageEvent(LivingEntity entity, DamageSource source, float damage) {
        Entity attacker = source.getEntity();
        if (entity.hasEffect(VWEffects.WIND_VEIL)) {
            return windVeilEffect(entity, source, damage);
        }

        if (attacker instanceof Player attackerPlayer && entity instanceof Player player) {
            if (PlayerAttachment.trustOther(attackerPlayer, player)) return false;
        }

        if (entity instanceof Wolf wolf) {
            boolean negateFreezingDMG = source.is(DamageTypeTags.IS_FREEZING) && VWEnchantments.getIgnition(wolf) > 0;
            boolean disableAccidentalDMG = wolf.getOwner() instanceof LivingEntity owner && attacker instanceof LivingEntity mob && owner == mob;

            if (negateFreezingDMG) {
                return false;
            }
            if (disableAccidentalDMG) {
                return false;
            }

            return VerdantWindBlessing.triggerBenediction(wolf, source, damage);

        } else if (entity instanceof Player player) {
            boolean disableAccidentalDMG = attacker instanceof Wolf wolf && wolf.getOwner() == player;
            if (disableAccidentalDMG) {
                return false;
            }
            return VerdantWindBlessing.triggerBenediction(player, source, damage);

        } else {
            return true;
        }
    }


    private static boolean windVeilEffect(LivingEntity entity, DamageSource damageSource, float damage) {
        if (!(entity.level() instanceof ServerLevel level)) return true;
        Entity source = damageSource.getEntity();
        if (entity instanceof Monster) return true;
        if (source instanceof LivingEntity attacker && !attacker.is(EntityTypes.WOLF)) {
            if (entity instanceof Player || entity instanceof Wolf) {
                RandomSource random = level.getRandom();
                DamageSource dmg = level.damageSources().magic();
                if (random.nextFloat() < 0.6f) {
                    if (VWEnchantments.getBenediction(entity)) {
                        attacker.hurtServer(level, dmg, Math.max(2, attacker.getHealth() * 0.15f));
                        if (attacker instanceof Player player && !player.getMainHandItem().isEmpty()) {
                            player.getCooldowns().addCooldown(player.getMainHandItem(), 60);
                        }
                    }
                    attacker.knockback(Math.max(1, attacker.getMaxHealth() * 0.05f), - attacker.getYHeadRot(), - entity.getYHeadRot(), dmg, 0);
                    level.playSound(null, entity.blockPosition(), SoundEvents.THORNS_HIT, SoundSource.PLAYERS);

                    for (int i = 0; i < random.nextIntBetweenInclusive(1, 5); i++) {
                        level.sendParticles(ParticleTypes.ENCHANT, entity.getX(), entity.getY(), entity.getZ(), 3, 0, 0, 0, 0);
                    }
                }
                if (entity instanceof Player player) {
                    List<Wolf> wolves = level.getEntities(EntityTypes.WOLF, scanArea(player, 16), wolf -> wolf.getOwner() == player);

                    if (!wolves.isEmpty()) {
                        for (Wolf wolf : wolves) {
                            wolf.setTarget(attacker);
                        }
                    }
                }
            }
        }
        return false;
    }
    private static void updateNearbyWolvesOnDamage(Player player, Player attacker) {
        ServerLevel level = (ServerLevel) player.level();
        List<Wolf> wolves = level.getEntitiesOfClass(Wolf.class, scanArea(player, 16), wolf -> wolf.getOwner() == player);

        if (wolves.isEmpty()) return;

        for (Wolf wolf : wolves) {
            List<Pair<String, UUID>> trustedPlayers = new ArrayList<>(WolfAttachment.getTrustedPlayers(wolf));
            UUID attackerUUID = attacker.getAttachedOrElse(VWAttachments.WOLF_PLAYER_SHARED_ID, attacker.getUUID());

            for (Pair<String, UUID> trusted : trustedPlayers) {
                if (trusted.getB().equals(attackerUUID)) {
                    trustedPlayers.remove(trusted);
                    wolf.setAttached(WolfAttachment.TRUSTED_PLAYERS, trustedPlayers);
                    sendToChat(player, VWColors.BLOODLUST_EFFECT_MUTED, false, wolf.getPlainTextName() + " no longer trust " + trusted.getA());
                    break;
                }
            }
        }
    }
}
