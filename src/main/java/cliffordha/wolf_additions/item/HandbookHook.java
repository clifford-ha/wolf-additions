package cliffordha.wolf_additions.item;

import cliffordha.wolf_additions.client.screen.HandbookScreen;
import cliffordha.wolf_additions.entity.player.PlayerStats;
import cliffordha.wolf_additions.entity.wolf.WolfStats;
import cliffordha.wolf_additions.item.handbooks.VWEffectsHandbook;
import cliffordha.wolf_additions.item.handbooks.VWEnchantmentsHandbook;
import cliffordha.wolf_additions.item.handbooks.VWFeaturesHandbook;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;

import static cliffordha.wolf_additions.item.TextColors.*;
import static cliffordha.wolf_additions.util.VWUtil.TextUtil.*;

public class HandbookHook {
    public static final String ENCHANTMENTS_HANDBOOK_TITLE = "§lEnchantments Handbook§r";
    public static final String EFFECTS_HANDBOOK_TITLE = "§lEffects Handbook§r";
    public static final String FEATURES_HANDBOOK_TITLE = "§lFeatures Handbook§r";

    public static final String STATUS_CHECK = "§lStatus Check§r";

    public static final String[] invalidPlayer = addPage(
            cText(RED, "Oopsies! The player who called this action is missing or null :3"));

    public static void use(Player player, int pageID) {
        switch (pageID) {
            case 2006 -> openScreen(ENCHANTMENTS_HANDBOOK_TITLE, VWEnchantmentsHandbook.ENCHANTMENTS_HANDBOOK_CONTENTS(player));
            case 2007 -> openScreen(EFFECTS_HANDBOOK_TITLE, VWEffectsHandbook.EFFECTS_HANDBOOK_CONTENTS());
            case 2008 -> openScreen(FEATURES_HANDBOOK_TITLE, VWFeaturesHandbook.FEATURES_HANDBOOK_CONTENTS());
            default -> {
            }
        }

        float random = Math.min(player.getRandom().nextFloat() + 0.5f, 1.0f);
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, player.getSoundSource(), random, random);
    }
    public static void showPlayerStats(Player player) {
        PlayerStatHook.resolvePlayer(player);
        var stat = PlayerStats.valueOf(player);
        openScreen(stat.name() + "'s Stats", PlayerStatHook.getPlayerStats());
    }
    public static void showWolfStats(Wolf wolf) {
        var stat = WolfStats.valueOf(wolf);
        openScreen(stat.name() + "'s Stats", PlayerStatHook.wolfStats(stat));
    }

    private static void openScreen(String title, String[] pages) {
        switch (title) {
            case ENCHANTMENTS_HANDBOOK_TITLE -> setScreen(ENCHANTMENTS_HANDBOOK_TITLE, pages);
            case EFFECTS_HANDBOOK_TITLE -> setScreen(EFFECTS_HANDBOOK_TITLE, pages);
            case FEATURES_HANDBOOK_TITLE -> setScreen(FEATURES_HANDBOOK_TITLE, pages);
            default -> setScreen(title, pages);
        }
    }

    @Environment(EnvType.CLIENT)
    private static void setScreen(String title, String[] pages) {
        Minecraft.getInstance().setScreenAndShow(new HandbookScreen(title, pages));
    }
}