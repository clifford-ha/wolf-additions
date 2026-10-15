package cliffordha.wolf_additions.config;

import cliffordha.wolf_additions.TOTVW;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = TOTVW.MOD_ID)
public class VWConfig implements ConfigData {
    public boolean DUMMY = true;
    public boolean CLIENT_ALLOW_EFFECT_OVERLAYS = true;
    public boolean CLIENT_BLOODLUST_EFFECT_OVERLAY = false;
    public boolean CLIENT_ENABLE_NOTIFIERS = true;
    public boolean CLIENT_SHOW_ATROCITY_COUNTER = false;

    public boolean SERVER_WOLF_DMG_DISTRIBUTION = true;
    public int SERVER_WOLF_PLAYER_SCAN_DISTANCE = 16;
    public int SERVER_BENEDICTION_HEALTH_THRESHOLD = 30;
    public boolean SERVER_ALWAYS_TRIGGER_BLESSING = false;
    public boolean SERVER_WOLF_SHARES_BENEDICTION_STACK = true;
    public boolean SERVER_TELEPORT_AFTER_SAVE = true;
    public int SERVER_WOLF_TP_METHOD = 0;
    public int SERVER_PLAYER_TP_METHOD = 0;
    public boolean SERVER_WOLF_TP_ALL = false;

    public boolean SERVER_SKILL_COOLDOWNS = true;
    public boolean SERVER_ITEM_COOLDOWNS = true;
    public boolean SERVER_OTHER_COOLDOWNS = true;



    public boolean DEBUG_PRINT_LOGS = false;

    public boolean LOG_ENCHANTMENT_SHOW_PLAYER_CD = false;
    public boolean LOG_ENCHANTMENT_SHOW_WOLF_CD = false;

    public static VWConfig get() {
        return AutoConfig.getConfigHolder(VWConfig.class).getConfig();
    }

    public static void load() {
        AutoConfig.register(VWConfig.class, Toml4jConfigSerializer::new);
    }

    public static void save() {
        AutoConfig.getConfigHolder(VWConfig.class).save();
    }
}