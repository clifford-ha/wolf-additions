package cliffordha.wolf_additions.item.handbooks;

import static cliffordha.wolf_additions.item.TextColors.*;
import static cliffordha.wolf_additions.item.TextFormatting.*;
import static cliffordha.wolf_additions.util.VWUtil.TextUtil.*;

public class VWFeaturesHandbook {
    private static final String descBenediction = "Benediction of the Verdant Mountains";

    private static final String titleBenedictionReturnPosition = cText(AQUA, bold("Benediction Return Position"));
    private static final String titleRevivalByProxy = cText(AQUA, bold("Revival by Proxy"));
    private static final String titleTrustSystem = cText(GOLD, bold("Trust System"));

    private static String italic(String t) {
        return fText(ITALIC, t);
    }
    private static String bold(String t) {
        return fText(BOLD, t);
    }

    private static String revivalByProxy() {
        return titleRevivalByProxy.toUpperCase() + nextLine
                + "A mechanic allows a player to be revived by their wolf companion when they get incapacitated and have no Totem of Undying in their mainhand or offhand slot (enabled by default in the settings). This can be triggered in the following ways:"
                + nextParagraph

                + "• Primary Trigger: Player gets incapacitated and their wolf companion is within the scan radius with more than one Benediction Stack. The scan radius is based on Max Chunk Scan in the settings (server).";
    }
    private static String benedictionSpawnpointInfo() {
        return titleBenedictionReturnPosition.toUpperCase() + nextLine
                + "When a wolf has the " + descBenediction + " enchantment, the player will be able to set a return point for the wolf. When the wolf gets knocked down and said return point is present and valid, and Teleport After Save is enabled, wolf will atempt to teleport to thet return point if it is valid.";
    }
    private static String trustSystemInfo() {
        return titleTrustSystem.toUpperCase() + nextLine
                + "Reintroduced from earlier versions of the mod. To utilize this feature, a player can use a Paper or Verixium Paper towards a fellow player by right clicking them. A text prompt should notify you upon succeeding. These Trust System is currently being used for Wolf-Player interaction."
                + nextParagraph

                + "When a player has a tamed wolf, they can use the command to perform various actions like syncing the data from player to wolf: " + bold("[  /wolf-additions trustData wolf sync  ]")
                + nextParagraph

                + "The player can also remove all or specific trusted players from their data. You need to query";
    }


    public static String[] FEATURES_HANDBOOK_CONTENTS() {
        return addPage(
                fText(BOLD, "INTRODUCTION") + nextLine
                        + "A list of new features and mechanics that aren't introduced in other handbooks."
                        + addSeparator
                        + revivalByProxy()
                        + addSeparator
                        + benedictionSpawnpointInfo()
                + addSeparator

        );
    }
}
