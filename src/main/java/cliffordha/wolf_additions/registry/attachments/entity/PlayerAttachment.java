package cliffordha.wolf_additions.registry.attachments.entity;

import cliffordha.wolf_additions.registry.attachments.VWAttachments;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.UUID;

import static cliffordha.wolf_additions.registry.attachments.AttachmentUtil.*;

public class PlayerAttachment {
    private static final String P = "player_";
    
    public static final AttachmentType<Boolean> IS_DEV_MODE = registerBool(P + "is_dev_mode", false);
    public static final AttachmentType<Integer> RANDOM_INT_10 = registerInt(P + "random_int_10", false);

    public static final AttachmentType<Integer> RECEIVED_ENCHANTMENTS_HANDBOOK = registerInt(P + "received_enchantments_handbook", false);
    public static final AttachmentType<Integer> RECEIVED_EFFECTS_HANDBOOK = registerInt(P + "received_effects_handbook", false);
    public static final AttachmentType<Integer> RECEIVED_FEATURES_HANDBOOK = registerInt(P + "received_features_handbook", false);

    public static final AttachmentType<Integer> CD_BLESSING_OF_THE_VERDANT_WIND = registerInt(P + "cd_blessing_of_the_verdant_wind", true);
    public static final AttachmentType<Integer> NOTIFY_BLESSING_OF_THE_VERDANT_WIND = registerInt(P + "notify_blessing_of_the_verdant_wind", false);

    public static final AttachmentType<Integer> VILLAGER_ATROCITY_COUNT = registerInt(P + "villager_atrocity_count", false);
    public static final AttachmentType<Integer> WOLF_ATROCITY_COUNT = registerInt(P + "wolf_atrocity_count", false);

    public static final AttachmentType<BlockPos> RESPAWN_POINT = registerBlockPos(P + "respawn_point", true);
    public static final AttachmentType<List<Pair<String, UUID>>> TRUSTED_PLAYERS = registerListPair(P + "trusted_players", true);

    public static Pair<String, UUID> getNameAndUUID(LivingEntity entity) {
        return new Pair<>(entity.getPlainTextName(), VWAttachments.getWolfPlayerSharedId(entity));
    }
    public static List<Pair<String, UUID>> getTrustedPlayers(Player player) {
        return player.getAttachedOrElse(TRUSTED_PLAYERS, List.of());
    }
    public static boolean trustOther(Player player, Player other) {
        List<Pair<String, UUID>> playerData = getTrustedPlayers(player);
        List<Pair<String, UUID>> otherData = getTrustedPlayers(other);

        List<Pair<String, UUID>> checkA = playerData.stream().filter(data -> data.getB().equals(VWAttachments.getWolfPlayerSharedId(other))).toList();
        List<Pair<String, UUID>> checkN = otherData.stream().filter(data -> data.getB().equals(VWAttachments.getWolfPlayerSharedId(player))).toList();

        return !checkA.isEmpty() && !checkN.isEmpty();
    }
}
