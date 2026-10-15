package cliffordha.wolf_additions.item;

import cliffordha.wolf_additions.entity.player.PlayerStats;
import cliffordha.wolf_additions.entity.wolf.WolfStats;
import net.minecraft.world.entity.player.Player;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.UUID;

import static cliffordha.wolf_additions.util.VWUtil.TextUtil.*;

public class PlayerStatHook {
    public static Player player;
    public static void resolvePlayer(Player resolver) {
        player = resolver;
    }
    public static String[] getPlayerStats() {
        if (player == null) return HandbookHook.invalidPlayer;
        return playerStats();
    }
    public static String[] wolfStats(WolfStats stats) {
        return addPage(
                "UUID: " + stats.UUID() + nextLine
                + "Soul ID: " + stats.soulID() + nextLine
                + "Family ID: " + stats.familyID() + nextLine
                + "Owner: " + stats.owner() + nextLine
                + "Owner UUID: " + stats.ownerUUID() + nextLine
                + "Shared UUID: " + stats.sharedUUID() + nextParagraph

                + "Benediction Stack: " + stats.benedictionStack() + nextLine
                + "Attack Cycle: " + stats.attackCycle() + nextLine
                + "Try Save Points: " + stats.trySavePoints() + nextLine
                + "Return Point: " + stats.returnPoint() + nextLine

                + "Trusted Players: " + nextLine + getPairs(stats.trustedPlayers())

        );
    }
    private static String[] playerStats() {
        var stat = PlayerStats.valueOf(player);

        return addPage(
                "UUID: " + stat.UUID() + nextLine
                + "SHARED UUID: " + stat.sharedUUID()
                + nextParagraph

                + "Atrocity Count [Wolf]: " + stat.wolfAtrocityCount() + nextLine
                + "Atrocity Cound [Villager]: " + stat.villagerAtrocityCount()
                + nextParagraph

                + "TRUSTED PLAYER DATA: " + nextLine + getPairs(stat.trustedPlayers())
                + nextParagraph

                + "Has received handbook for: " + nextLine
                + "Enchantments: " + stat.hasReceivedEnchantmentsHandbook() + nextLine
                + "Features: " + stat.hasReceivedFeaturesHandbook() + nextLine
                + "Effects: " + stat.hasReceivedEffectsHandbook()
        );
    }
    private static String getPairs(List<Pair<String, UUID>> list) {
        return list.stream().map(pair -> pair.getA() + " " + pair.getB()).reduce((a, b) -> a + nextLine + b).orElse("");
    }
}
