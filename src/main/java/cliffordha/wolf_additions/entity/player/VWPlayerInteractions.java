package cliffordha.wolf_additions.entity.player;

import cliffordha.wolf_additions.item.HandbookHook;
import cliffordha.wolf_additions.item.events.VWItemBlessings;
import cliffordha.wolf_additions.registry.VWEnchantments;
import cliffordha.wolf_additions.registry.VWSounds;
import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import cliffordha.wolf_additions.tag.VWItemTags;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static cliffordha.wolf_additions.util.VWUtil.sendToChat;

public class VWPlayerInteractions {
    public static void onInteractEvents() {
        UseEntityCallback.EVENT.register( (player, level, hand, entity, _) -> onEntityInteractEvent(player, entity));
        UseItemCallback.EVENT.register((player, level, hand) -> onUseItemEvent(player));
    }

    private static InteractionResult onEntityInteractEvent(Player player, Entity entity) {
        if (!(entity instanceof LivingEntity)) return InteractionResult.PASS;

        ItemStack stack = player.getMainHandItem();

        if (player.level().isClientSide()) {
            if (stack.is(Items.WRITTEN_BOOK) && entity instanceof Wolf wolf && wolf.getOwner() == player) {
                if (hasName(HandbookHook.STATUS_CHECK, stack)) {
                    HandbookHook.showWolfStats(wolf);
                    return InteractionResult.SUCCESS;
                }
            }
        } else {

            if (stack.is(Items.PAPER) && entity instanceof Player otherPlayer) {
                if (player == otherPlayer) return InteractionResult.PASS;

                AttachmentType<List<Pair<String, UUID>>> T_LIST = PlayerAttachment.TRUSTED_PLAYERS;
                List<Pair<String, UUID>> oldPlayerData = player.getAttachedOrElse(T_LIST, List.of());
                List<Pair<String, UUID>> newPlayerData = new ArrayList<>(oldPlayerData);

                Pair<String, UUID> mobData = PlayerAttachment.getNameAndUUID(otherPlayer);

                boolean hasPriorData = oldPlayerData.stream().anyMatch(data -> data.getB() == mobData.getB());
                if (oldPlayerData.size() >= 4 && !hasPriorData) {
                    sendToChat(player, true, "You can only list 4 trusted players at a time.");
                    return InteractionResult.FAIL;
                }
                if (hasPriorData) {
                    var index = oldPlayerData.stream().findFirst().filter(data -> data.getB() == mobData.getB()).map(oldPlayerData::indexOf).orElse(null);
                    if (index == null) return InteractionResult.PASS;

                    Pair<String, UUID> data = oldPlayerData.get(index);

                    if (!data.getA().equals(mobData.getA()) && data.getB() == mobData.getB()) {
                        Pair<String, UUID> update = new Pair<>(mobData.getA(), mobData.getB());

                        newPlayerData.set(newPlayerData.indexOf(data), update);
                        player.setAttached(T_LIST, List.copyOf(newPlayerData));
                        sendToChat(player, true, "Updated name for " + data.getA() + " to " + update.getA() + ".");
                        return InteractionResult.SUCCESS;

                    } else {
                        sendToChat(player, true, "You already trust " + mobData.getA());
                        return InteractionResult.FAIL;
                    }
                } else {
                    newPlayerData.add(mobData);
                    player.setAttached(PlayerAttachment.TRUSTED_PLAYERS, List.copyOf(newPlayerData));

                    sendToChat(player, false, otherPlayer.getPlainTextName() + " is now a trusted player!");
                    player.playSound(VWSounds.NOTIFY);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult onUseItemEvent(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        if (player.level().isClientSide()) {

            if (mainHand.is(Items.WRITTEN_BOOK)) {
                boolean pass = false;
                if (hasName(HandbookHook.ENCHANTMENTS_HANDBOOK_TITLE, mainHand)) {
                    HandbookHook.use(player, 2006);
                    pass = true;
                } else if (hasName(HandbookHook.EFFECTS_HANDBOOK_TITLE, mainHand)) {
                    HandbookHook.use(player, 2007);
                    pass = true;
                } else if (hasName(HandbookHook.FEATURES_HANDBOOK_TITLE, mainHand)) {
                    HandbookHook.use(player, 2008);
                    pass = true;
                } else if (hasName(HandbookHook.STATUS_CHECK, mainHand)) {
                    HandbookHook.showPlayerStats(player);
                    pass = true;
                }
                if (pass) return InteractionResult.SUCCESS;
            }
        } else {

            boolean BENEDICTION_SUPPORTED_ITEMS = (mainHand.tags().anyMatch(Predicate.isEqual(VWItemTags.BENEDICTION_ENCHANTMENT_USE_QUALIFIED_TOOLS))
                    || mainHand.tags().anyMatch(Predicate.isEqual(VWItemTags.BENEDICTION_ENCHANTMENT_USE_QUALIFIED_ITEMS)));

            boolean HAS_BENEDICTION = VWEnchantments.getBenediction(player);

            if (BENEDICTION_SUPPORTED_ITEMS && HAS_BENEDICTION && player.isCrouching()) {
                boolean applied = VWItemBlessings.tryApply(player);
                if (applied) return InteractionResult.SUCCESS_SERVER;
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean hasName(String name, ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        Component components = stack.getComponents().get(DataComponents.CUSTOM_NAME);
        if (components == null) return false;
        return components.getString().equals(name);
    }
}
