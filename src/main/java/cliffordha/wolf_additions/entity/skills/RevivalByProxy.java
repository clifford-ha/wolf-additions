package cliffordha.wolf_additions.entity.skills;

import cliffordha.wolf_additions.config.VWConfig;
import cliffordha.wolf_additions.registry.VWColors;
import cliffordha.wolf_additions.registry.attachments.PlayerPrefs;
import cliffordha.wolf_additions.registry.attachments.entity.WolfAttachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

import static cliffordha.wolf_additions.util.VWUtil.*;

public class RevivalByProxy {
    public static boolean revivePlayerIfPossible(LivingEntity entity, DamageSource damageSource, float damage) {
        boolean hasTotem = entity.getMainHandItem().is(Items.TOTEM_OF_UNDYING) || entity.getOffhandItem().is(Items.TOTEM_OF_UNDYING);
        if (entity instanceof Player player && !hasTotem) {
            if (damageSource.is(DamageTypes.GENERIC_KILL)) return true;
            if (!player.getAttachedOrElse(PlayerPrefs.BENEDICTION_SHARE_STACK, VWConfig.get().SERVER_WOLF_SHARES_BENEDICTION_STACK)) return true;

            Level getLevel = player.level();
            ServerLevel level = (ServerLevel) getLevel;

            double distance = VWConfig.get().SERVER_WOLF_PLAYER_SCAN_DISTANCE * 16;
            AttachmentType<Integer> BENEDICTION_STACK = WolfAttachment.BENEDICTION;

            List<Wolf> wolves = level.getEntities(EntityTypes.WOLF, player.getBoundingBox().inflate(distance), wolf ->
                    wolf.getOwner() != null && wolf.getOwner().is(player));

            if (!wolves.isEmpty()) {
                List<Wolf> wolfWithStack = wolves.stream().filter(wolf -> wolf.getAttachedOrElse(BENEDICTION_STACK, 0) > 1).toList();
                if (!wolfWithStack.isEmpty()) {
                    Wolf mainWolf = wolfWithStack.getFirst();
                    getTeleportToWolf(player, mainWolf, false, mainWolf);

                    if (player.getAttachedOrElse(PlayerPrefs.BENEDICTION_WOLF_TP_ALL, VWConfig.get().SERVER_WOLF_TP_ALL)) {
                        for (Wolf wolf : wolves) {
                            getTeleportToWolf(player, wolf, true, mainWolf);
                        }
                        for (Wolf otherMainWolf : wolfWithStack) {
                            getTeleportToWolf(player, otherMainWolf, true, mainWolf);
                        }
                    }

                    VerdantWindBlessing.applyBenedictionEffects(player, true);

                    int benediction = mainWolf.getAttachedOrElse(BENEDICTION_STACK, 0);
                    mainWolf.setAttached(BENEDICTION_STACK, benediction - 1);

                    String name = mainWolf.getPlainTextName();
                    int STACK_AFTER = mainWolf.getAttachedOrElse(BENEDICTION_STACK, 0);
                    if (STACK_AFTER == 0) {
                        sendToChat(mainWolf, VWColors.BLOODLUST_EFFECT_MUTED, name + " used up all Benediction stacks");
                    } else {
                        sendToChat(mainWolf, VWColors.VERDANT_WIND_MUTED, "A Benediction stack has been shared by " + name + ". " + STACK_AFTER + " remaining.");
                    }

                    mainWolf.makeSound(new SoundEvent(Identifier.withDefaultNamespace("entity.wolf.whine"), Optional.of(16.0f)));
                    level.broadcastEntityEvent(player, (byte) 35);
                    return false;
                }
            }
        }
        return true;
    }
    private static void getTeleportToWolf(Player player, Wolf wolf, boolean tpAll, Wolf mainWolf) {
        if (!player.canTeleport(player.level(), wolf.level())) return;
        if (!player.getAttachedOrElse(PlayerPrefs.BENEDICTION_TELEPORT_AFTER_SAVE, VWConfig.get().SERVER_TELEPORT_AFTER_SAVE)) return;
        if (player.distanceTo(wolf) < 16) return;

        BlockPos wolfPos = wolf.blockPosition();
        BlockPos playerPos = player.blockPosition();
        boolean tpMode = player.getAttachedOrElse(PlayerPrefs.BENEDICTION_PLAYER_TP_METHOD, VWConfig.get().SERVER_PLAYER_TP_METHOD) < 1;

        if (tpAll) {
            BlockPos mainWolfPos = mainWolf.blockPosition();
            if (tpMode) {
                if (isNotValidForTP(mainWolf.level(), mainWolfPos)) return;
                player.teleportTo(mainWolfPos.getX(), mainWolfPos.getY(), mainWolfPos.getZ());
                wolf.teleportToAroundBlockPos(mainWolfPos);
            } else {
                if (isNotValidForTP(player.level(), playerPos)) return;
                wolf.teleportToAroundBlockPos(playerPos);
                mainWolf.teleportToAroundBlockPos(mainWolfPos);
            }
            untetherWolf(mainWolf);
        } else {
            if (tpMode) {
                if (isNotValidForTP(wolf.level(), wolfPos)) return;
                player.teleportTo(wolfPos.getX(), wolfPos.getY(), wolfPos.getZ());
            } else {
                if (isNotValidForTP(player.level(), playerPos)) return;
                wolf.teleportToAroundBlockPos(playerPos);
            }
        }
        untetherWolf(wolf);
    }
    private static void untetherWolf(Wolf wolf) {
        wolf.unRide();
        wolf.dropLeash();
        wolf.setOrderedToSit(false);
    }
}
