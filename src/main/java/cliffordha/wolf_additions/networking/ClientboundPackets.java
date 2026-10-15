package cliffordha.wolf_additions.networking;

import cliffordha.wolf_additions.config.VWConfig;
import cliffordha.wolf_additions.networking.packets.ClientPrefsPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ClientboundPackets {
    public static void registerPackets() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
                sender.sendPacket(new ClientPrefsPayload(
                        VWConfig.get().CLIENT_SHOW_ATROCITY_COUNTER,
                        VWConfig.get().CLIENT_ENABLE_NOTIFIERS,

                        VWConfig.get().SERVER_BENEDICTION_HEALTH_THRESHOLD,
                        VWConfig.get().SERVER_WOLF_SHARES_BENEDICTION_STACK,
                        VWConfig.get().SERVER_ALWAYS_TRIGGER_BLESSING,
                        VWConfig.get().SERVER_TELEPORT_AFTER_SAVE,
                        VWConfig.get().SERVER_WOLF_TP_METHOD,
                        VWConfig.get().SERVER_PLAYER_TP_METHOD,
                        VWConfig.get().SERVER_WOLF_TP_ALL
                ))
        );
    }
}
