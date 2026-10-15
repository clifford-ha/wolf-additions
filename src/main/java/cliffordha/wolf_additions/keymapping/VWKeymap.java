package cliffordha.wolf_additions.keymapping;

import cliffordha.wolf_additions.TOTVW;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class VWKeymap {
    private static final String PREFIX = "key.wolf_additions.";
    public static final String TOTVW_CONFIG_SCREEN = registerKey("wolf_config");

    public static final KeyMapping WOLF_CONFIG = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(TOTVW_CONFIG_SCREEN, InputConstants.KEY_LBRACKET, KeyMapping.Category.GAMEPLAY));


    public static boolean isWolfAdditionsConfigPressed() {
        return !WOLF_CONFIG.isUnbound() && WOLF_CONFIG.isDown();
    }

    public static void register() {
        TOTVW.sendClassRegisterLog("KeyMappings");
    }
    private static String registerKey(String key) {
        return PREFIX + key;
    }
}
