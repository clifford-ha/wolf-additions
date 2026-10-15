package cliffordha.wolf_additions.entity.player;

import cliffordha.wolf_additions.registry.attachments.AttachmentUtil;
import cliffordha.wolf_additions.registry.attachments.VWAttachments;
import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import net.minecraft.world.entity.player.Player;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.UUID;

public record PlayerStats(
        String name,
        String UUID,
        String sharedUUID,

        int wolfAtrocityCount,
        int villagerAtrocityCount,

        List<Pair<String, UUID>> trustedPlayers,

        boolean hasReceivedEnchantmentsHandbook,
        boolean hasReceivedFeaturesHandbook,
        boolean hasReceivedEffectsHandbook
) {
    public static PlayerStats valueOf(Player player) {
        var sharedID = player.getAttachedOrElse(VWAttachments.WOLF_PLAYER_SHARED_ID, AttachmentUtil.EMPTY_UUID);
        String SHARED_UUID = sharedID != AttachmentUtil.EMPTY_UUID ? String.valueOf(sharedID) : "None";

        boolean enchantmentHandbook = player.getAttachedOrElse(PlayerAttachment.RECEIVED_ENCHANTMENTS_HANDBOOK, 0) > 0;
        boolean featureHandbook = player.getAttachedOrElse(PlayerAttachment.RECEIVED_FEATURES_HANDBOOK, 0) > 0;
        boolean effectHandbook = player.getAttachedOrElse(PlayerAttachment.RECEIVED_EFFECTS_HANDBOOK, 0) > 0;

        return new PlayerStats(
                player.getPlainTextName(),
                player.getStringUUID(),
                SHARED_UUID,

                player.getAttachedOrElse(PlayerAttachment.WOLF_ATROCITY_COUNT, 0),
                player.getAttachedOrElse(PlayerAttachment.VILLAGER_ATROCITY_COUNT, 0),

                PlayerAttachment.getTrustedPlayers(player),

                enchantmentHandbook,
                featureHandbook,
                effectHandbook
        );
    }
}
