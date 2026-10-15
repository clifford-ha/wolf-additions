package cliffordha.wolf_additions.registry.attachments;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.registry.attachments.entity.*;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.UUID;

import static cliffordha.wolf_additions.registry.attachments.AttachmentUtil.*;

public class VWAttachments {
    public static final AttachmentType<UUID> WOLF_PLAYER_SHARED_ID = registerUUID("wolf_player_shared_id", true);
    public static final AttachmentType<Integer> ATTACK_CYCLE = WolfAttachment.ATTACK_CYCLE;

    public static UUID getWolfPlayerSharedId(LivingEntity entity) {
        return entity.getAttachedOrElse(WOLF_PLAYER_SHARED_ID, entity.getUUID());
    }

    public static final List<AttachmentType<?>> WOLF_ATTACHMENTS = List.of(
            WolfAttachment.TIMER_AIR_SUPPLY,
            WolfAttachment.NOTIFY_AIR_SUPPLY,
            WolfAttachment.CD_BLESSING_OF_THE_VERDANT_WIND,
            WolfAttachment.CD_BLOODLUST_SKILL_PARALYZE,
            WolfAttachment.CD_MIGHT_SKILL_RUPTURE,
            WolfAttachment.CD_IGNORE_HIGH_DAMAGE,
            WolfAttachment.NOTIFY_MIGHT_SKILL_RUPTURE,
            WolfAttachment.NOTIFY_BLOODLUST_SKILL_PARALYZE,
            WolfAttachment.NOTIFY_BLESSING_OF_THE_VERDANT_WIND,
            WolfAttachment.BENEDICTION,
            WolfAttachment.SOUL_ID,
            WolfAttachment.FAMILY_ID,
            WolfAttachment.TRY_SAVE_POINTS,
            WolfAttachment.TRY_SAVE_STATUS,
            WolfAttachment.RESPAWN_POINT,
            WolfAttachment.TRUSTED_PLAYERS,
            WolfAttachment.ATTACK_CYCLE
    );
    public static final List<AttachmentType<?>> PLAYER_ATTACHMENTS = List.of(
            PlayerAttachment.IS_DEV_MODE,

            PlayerAttachment.RECEIVED_ENCHANTMENTS_HANDBOOK,
            PlayerAttachment.RECEIVED_EFFECTS_HANDBOOK,
            PlayerAttachment.RECEIVED_FEATURES_HANDBOOK,

            PlayerAttachment.VILLAGER_ATROCITY_COUNT,
            PlayerAttachment.WOLF_ATROCITY_COUNT,
            PlayerAttachment.CD_BLESSING_OF_THE_VERDANT_WIND,
            PlayerAttachment.NOTIFY_BLESSING_OF_THE_VERDANT_WIND,
            PlayerAttachment.RESPAWN_POINT,
            PlayerAttachment.TRUSTED_PLAYERS
    );
    public static final List<AttachmentType<?>> PLAYER_PREFS = List.of(
            PlayerPrefs.SHOW_ATROCITY_COUNTER,
            PlayerPrefs.ENABLE_NOTIFIERS,

            PlayerPrefs.BENEDICTION_HEALTH_THRESHOLD,
            PlayerPrefs.BENEDICTION_SHARE_STACK,
            PlayerPrefs.BENEDICTION_ALWAYS_TRIGGER_BLESSING,
            PlayerPrefs.BENEDICTION_TELEPORT_AFTER_SAVE,
            PlayerPrefs.BENEDICTION_WOLF_TP_METHOD,
            PlayerPrefs.BENEDICTION_PLAYER_TP_METHOD,
            PlayerPrefs.BENEDICTION_WOLF_TP_ALL,
            PlayerPrefs.BENEDICTION_FORCE_TP
    );
    public static final int TOTAL = WOLF_ATTACHMENTS.size() + PLAYER_ATTACHMENTS.size() + PLAYER_PREFS.size();


    public static void register() {
        TOTVW.sendClassRegisterLog(
                "Custom Attachments (" +
                        "Wolf: " + WOLF_ATTACHMENTS.size() + ", " +
                        "Player: " + PLAYER_ATTACHMENTS.size() + ", " +
                        "PlayerPrefs: " + PLAYER_PREFS.size() + "): " +
                        TOTAL + " in total has been"
        );
    }
}