package cliffordha.wolf_additions.client;

import cliffordha.wolf_additions.item.HandbookHook;
import cliffordha.wolf_additions.registry.*;
import cliffordha.wolf_additions.tag.VWItemTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static cliffordha.wolf_additions.registry.VWEnchantments.entityEnchantmentLVL;

@Environment(EnvType.CLIENT)
public class VWTooltips {
    public static void register() {
        ItemTooltipCallback.EVENT.register(VWTooltips::onTooltip);
    }
    private static final Minecraft mc = Minecraft.getInstance();
    private static void onTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> lines) {
        if (mc.level == null) return;

        List<Component> injected = new ArrayList<>();

        handleBenedictionToolLines(stack, injected);
        handleBenedictionItemLines(stack, injected);
        handleLoreLines(stack, injected);

        if (!injected.isEmpty()) {
            int insertAt = Math.min(1, lines.size());
            lines.addAll(insertAt, injected);
        }
    }

    private static void handleBenedictionToolLines(ItemStack stack, List<Component> out) {
        if (stack.tags().noneMatch(Predicate.isEqual(VWItemTags.BENEDICTION_ENCHANTMENT_USE_QUALIFIED_TOOLS))) return;
        if (!hasBenediction()) return;

        String desc = resolveToolEffect(stack);
        if (desc != null) addVWItem(out, desc);
    }

    private static String resolveToolEffect(ItemStack stack) {
        if (stack.tags().anyMatch(t -> t.location().getPath().equals("swords")))   return "VW Strength";
        if (stack.tags().anyMatch(t -> t.location().getPath().equals("axes")))     return "VW Strength II";
        if (stack.tags().anyMatch(t -> t.location().getPath().equals("pickaxes"))) return "VW Haste";
        if (stack.tags().anyMatch(t -> t.location().getPath().equals("hoes")))     return "VW Absorption";
        if (stack.tags().anyMatch(t -> t.location().getPath().equals("shovels")))  return "VW Speed";
        return null;
    }


    private static void handleBenedictionItemLines(ItemStack stack, List<Component> out) {
        if (stack.tags().noneMatch(Predicate.isEqual(VWItemTags.BENEDICTION_ENCHANTMENT_USE_QUALIFIED_ITEMS))) return;
        if (!hasBenediction()) return;

        if (stack.is(Items.GLOWSTONE_DUST)) {
            addVWItem(out, "VW Night Vision");
        }
    }
    private static void handleLoreLines(ItemStack stack, List<Component> out) {
        if (mc.level == null) return;
        Player player = mc.player;
        if (player == null) return;

        if (stack.is(Items.WRITTEN_BOOK)) {
            Component component = stack.getComponents().get(DataComponents.CUSTOM_NAME);
            if (component == null) return;
            if (component.getString().equals(HandbookHook.ENCHANTMENTS_HANDBOOK_TITLE)
            || component.getString().equals(HandbookHook.EFFECTS_HANDBOOK_TITLE)
            || component.getString().equals(HandbookHook.FEATURES_HANDBOOK_TITLE)) {
                addEmpty(out);
                addText(out, VWColors.VERDANT_WIND, "Tales of the Verdant Wind");
                addText(out, VWColors.GRAY_MUTED, "By: Clifford HA");
            }
            return;
        }

        boolean ACTIVE_BENEDICTION = entityEnchantmentLVL(player, EquipmentSlot.CHEST, VWEnchantments.BENEDICTION_OF_THE_VERDANT_MOUNTAINS) > 0;

        if (ACTIVE_BENEDICTION) {
            if (stack.is(ItemTags.CHEST_ARMOR)) {
                if (stack.is(ItemTags.REPAIRS_WOLF_ARMOR) || stack.is(VWItemTags.WOLF_ARMOR_ENCHANTABLE)) return;
                addTextChestplate(out, "Grants effect when an item with blessing mark [VW] is used");
            }
        }
    }

    private static boolean hasBenediction() {
        if (mc.player == null || mc.level == null) return false;
        return entityEnchantmentLVL(mc.player, EquipmentSlot.CHEST, VWEnchantments.BENEDICTION_OF_THE_VERDANT_MOUNTAINS) > 0;
    }

    private static void addVWItem(List<Component> out, String... text) {
        if (mc.hasShiftDown()) {
            out.add(Component.literal(Arrays.toString(text)).withColor(VWColors.VERDANT_WIND));
        } else {
            out.add(Component.literal("[VW] Active"));
        }
    }

    private static void addTextChestplate(List<Component> out, String... keys) {
        if (mc.hasShiftDown()) {
            out.add(Component.literal("Verdant Wind's Blessing").withColor(VWColors.VERDANT_WIND));
            for (String key : keys) {
                List<FormattedText> wrapped = mc.font.getSplitter()
                        .splitLines(Component.translatable(key), 200, Style.EMPTY);
                for (FormattedText line : wrapped) {
                    out.add(Component.literal(line.getString())
                            .withColor(VWColors.VERDANT_WIND_MUTED)
                            .withStyle(ChatFormatting.ITALIC));
                }
            }
            out.add(Component.literal(""));
        } else {
            out.add(Component.literal("[VW] Blessing Active"));
        }
    }
    private static void addText(List<Component> out, String... keys) {
        for (String key : keys) {
            List<FormattedText> wrapped = mc.font.getSplitter()
                    .splitLines(Component.translatable(key), 150, Style.EMPTY);
            for (FormattedText line : wrapped) {
                out.add(Component.literal(line.getString())
                        .withColor(VWColors.GRAY));
            }
        }
    }
    private static void addText(List<Component> out, int color, String... keys) {
        getComponentKeys(out, color, keys);
    }
    private static void addEmpty(List<Component> out) {
        out.add(Component.literal(""));
    }
    private static void addExpandingText(List<Component> out, int color, String... keys) {
        if (mc.hasShiftDown()) {
            getComponentKeys(out, color, keys);
        } else {
            out.add(Component.literal("Read lore...").withColor(VWColors.GRAY));
        }
    }

    private static void getComponentKeys(List<Component> out, int color, String[] keys) {
        for (String key : keys) {
            List<FormattedText> wrapped = mc.font.getSplitter()
                    .splitLines(Component.translatable(key), 150, Style.EMPTY);
            for (FormattedText line : wrapped) {
                out.add(Component.literal(line.getString()).withColor(color));
            }
        }
    }
}