package cliffordha.wolf_additions.networking;

import cliffordha.wolf_additions.networking.packets.ClientPrefsPayload;
import cliffordha.wolf_additions.registry.attachments.PlayerPrefs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class ServerboundPackets {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ClientPrefsPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            player.level().getServer().execute(() -> {
                player.setAttached(PlayerPrefs.ENABLE_NOTIFIERS, payload.enableNotifiers());
                player.setAttached(PlayerPrefs.SHOW_ATROCITY_COUNTER, payload.showAtrocityCounter());

                player.setAttached(PlayerPrefs.BENEDICTION_HEALTH_THRESHOLD, payload.benedictionLowHealthThreshold());
                player.setAttached(PlayerPrefs.BENEDICTION_SHARE_STACK, payload.benedictionShareStack());
                player.setAttached(PlayerPrefs.BENEDICTION_ALWAYS_TRIGGER_BLESSING, payload.benedictionAlwaysTriggerBlessing());
                player.setAttached(PlayerPrefs.BENEDICTION_TELEPORT_AFTER_SAVE, payload.benedictionTeleportAfterSave());
                player.setAttached(PlayerPrefs.BENEDICTION_WOLF_TP_METHOD, payload.benedictionWolfTPMethod());
                player.setAttached(PlayerPrefs.BENEDICTION_PLAYER_TP_METHOD, payload.benedictionPlayerTPMethod());
                player.setAttached(PlayerPrefs.BENEDICTION_WOLF_TP_ALL, payload.benedictionWolfTPAll());
            });
        });
    }
}
