package cliffordha.wolf_additions.networking;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.networking.packets.ClientPrefsPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class VWNetworking {
    public static void registerServerboundPackets(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        registry.register(ClientPrefsPayload.TYPE, ClientPrefsPayload.STREAM_CODEC);

    }

    public static void register() {
        registerServerboundPackets(PayloadTypeRegistry.serverboundPlay());

        TOTVW.sendClassRegisterLog("Networking");
    }
}
