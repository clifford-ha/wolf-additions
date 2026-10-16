package cliffordha.wolf_additions.registry;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.item.HandbookHook;
import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import cliffordha.wolf_additions.registry.attachments.entity.WolfAttachment;
import cliffordha.wolf_additions.util.VWUtil;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import oshi.util.tuples.Pair;

import java.util.*;

import static cliffordha.wolf_additions.util.VWUtil.sendToChat;

public class VWCommands {
    private static final AttachmentType<List<Pair<String, UUID>>> TRUST_DATA = PlayerAttachment.TRUSTED_PLAYERS;
    private static final AttachmentType<List<Pair<String, UUID>>> WOLF_TRUST_DATA = WolfAttachment.TRUSTED_PLAYERS;

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("trust_data")
                    .then(Commands.literal("query")
                    .executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }

                        List<Pair<String, UUID>> data = player.getAttachedOrElse(TRUST_DATA, List.of());
                        if (!data.isEmpty()) {
                            String firstPart = data.size() > 1 ? "These are currently " : "There is currently ";
                            sendSuccess(context, false, firstPart + data.size() + " player" + (data.size() > 1 ? "s" : "") + " you trust.");

                            sendToChat(player, false, "Index  |  Name");
                            for (Pair<String, UUID> pair : player.getAttachedOrElse(TRUST_DATA, List.of())) {
                                sendToChat(player, false, data.indexOf(pair) + 1 + " " + pair.getA());
                            }

                            return data.size();
                        } else {
                            sendFail(context, VWColors.GRAY, "You don't have any trusted players in your list.");
                            return 0;
                        }
                    }
                    ))));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("trust_data")
                    .then(Commands.literal("remove")
                    .then(Commands.argument("entity", IntegerArgumentType.integer(1))
                    .executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }

                        int index = context.getArgument("entity", Integer.class);
                        List<Pair<String, UUID>> data = new ArrayList<>(player.getAttachedOrElse(TRUST_DATA, List.of()));

                        if (!data.isEmpty()) {
                            if (index < 1 || index > data.size()) {
                                sendFail(context, "The index you provided is out of range. (1-" + data.size() + ")");
                                return 0;
                            }

                            Pair<String, UUID> removed = data.get(index - 1);
                            sendSuccess(context, false, "You removed " + removed.getA() + " from your list of trusted players.");
                            data.remove(index - 1);
                            player.setAttached(TRUST_DATA, data);
                            return data.size();
                        } else {
                            sendFail(context, "No trust data to remove.");
                            return 0;
                        }
                    }
                    )))));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("trust_data")
                    .then(Commands.literal("remove")
                    .then(Commands.literal("all")
                    .executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }

                        int data = player.getAttachedOrElse(TRUST_DATA, List.of()).size();
                        if (data > 0) {
                            player.removeAttached(TRUST_DATA);
                            sendSuccess(context, false, "You removed your list of trusted players (" + data + ").");
                            return data;
                        } else {
                            sendFail(context, "No trust data to remove.");
                            return 0;
                        }
                    }
                    )))));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("trust_data")
                    .then(Commands.literal("wolf")
                    .then(Commands.literal("sync")
                    .executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }
                        ServerLevel level = player.level();

                        List<Wolf> wolves = level.getEntities(
                                EntityTypes.WOLF,
                                player.getBoundingBox().inflate(16),
                                t -> t.getOwner() == player);

                        if (wolves.isEmpty()) {
                            sendFail(context, "No nearby wolves to sync to!");
                            return 0;
                        }

                        List<Pair<String, UUID>> trustData = player.getAttachedOrElse(TRUST_DATA, List.of());
                        for (Wolf wolf : wolves) {
                            if (!trustData.isEmpty()) {
                                wolf.setAttached(WOLF_TRUST_DATA, trustData);

                            } else {
                                wolf.removeAttached(WOLF_TRUST_DATA);
                            }
                        }

                        String wolfCount = wolves.size() > 1 ? "nearby wolves" : wolves.getFirst().getPlainTextName();
                        String count = "Synced " + trustData.size() + " trusted players to " + wolfCount;

                        String finalize = trustData.isEmpty() ? "Removed old data from " + wolfCount : count;

                        sendSuccess(context, false, finalize);
                        return wolves.size();
                    }
                    )))));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("enchantments_handbook").executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }
                        int getStat = player.getAttachedOrElse(PlayerAttachment.RECEIVED_ENCHANTMENTS_HANDBOOK, 0);
                        if (getStat < 1) {
                            giveOrDropHandbook(player, 0);
                        } else {
                            if (player.isCreative() || player.isSpectator()) {
                                giveOrDropHandbook(player, 0);
                            } else {
                                handbookCopy(context);
                            }
                        }
                        return getStat;
                    }
                    )));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("effects_handbook").executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }
                        int getStat = player.getAttachedOrElse(PlayerAttachment.RECEIVED_EFFECTS_HANDBOOK, 0);
                        if (getStat < 1) {
                            giveOrDropHandbook(player, 1);
                        } else {
                            if (player.isCreative() || player.isSpectator()) {
                                giveOrDropHandbook(player, 1);
                            } else {
                                handbookCopy(context);
                            }
                        }
                        return getStat;
                    }
                    )));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("features_handbook").executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }

                        int getStat = player.getAttachedOrElse(PlayerAttachment.RECEIVED_FEATURES_HANDBOOK, 0);
                        if (getStat < 1) {
                            giveOrDropHandbook(player, 3);
                        } else {
                            if (player.isCreative() || player.isSpectator()) {
                                giveOrDropHandbook(player, 3);
                            } else {
                                handbookCopy(context);
                            }
                        }
                        return getStat;
                    }
                    )));

            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("get_atrocity_count").executes(context -> {
                                ServerPlayer player = getPlayer(context);
                                if (player == null) {
                                    failed(context);
                                    return 0;
                                }

                                int villager = player.getAttachedOrElse(PlayerAttachment.VILLAGER_ATROCITY_COUNT, 0);
                                int wolf = player.getAttachedOrElse(PlayerAttachment.WOLF_ATROCITY_COUNT, 0);
                                if ((villager + wolf) < 1) {
                                    context.getSource().sendSystemMessage(Component.literal("You don't have any atrocity count."));
                                } else {
                                    context.getSource().sendSuccess(() -> Component.literal("Wolf: " + wolf + "  |  Villager: " + villager), true);
                                }
                                return 1;
                            }
                    )));
        });


        if (!TOTVW.IN_DEVELOPMENT) return;
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("tame_nearby_wolves").executes(context -> {
                        ServerPlayer player = getPlayer(context);
                        if (player == null) {
                            failed(context);
                            return 0;
                        }

                        ServerLevel level = player.level();
                        List<Wolf> wolves = level.getEntities(EntityTypes.WOLF,
                                player.getBoundingBox().inflate(32),
                                wolf -> wolf.isTame() && wolf.getUUID() != player.getUUID());

                        if (wolves.isEmpty()) {
                            context.getSource().sendFailure(Component.literal("No nearby wolves to tame!"));
                            return 0;
                        }

                        for (Wolf wolf : wolves) {
                            wolf.setOwner(player);
                            wolf.setTame(true, true);
                        }
                        context.getSource().sendSuccess(() -> Component.literal("Tamed " + wolves.size() + " nearby wolves."), true);
                        return wolves.size();
                    }
                    )));
            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("release_tamed_wolves").executes(context -> {
                                ServerPlayer player = getPlayer(context);
                                if (player == null) {
                                    failed(context);
                                    return 0;
                                }

                                ServerLevel level = player.level();
                                List<Wolf> wolves = level.getEntities(EntityTypes.WOLF,
                                        player.getBoundingBox().inflate(32),
                                        wolf -> wolf.isTame() && wolf.getOwner() == player);

                                if (wolves.isEmpty()) {
                                    context.getSource().sendFailure(Component.literal("No nearby wolves to un-tame!"));
                                    return 0;
                                }

                                for (Wolf wolf : wolves) {
                                    wolf.setOwner(null);
                                    wolf.setTame(false, true);
                                }
                                context.getSource().sendSuccess(() -> Component.literal("Un-tamed " + wolves.size() + " nearby wolves."), true);
                                return wolves.size();
                            }
                    )));
            dispatcher.register(Commands.literal("wolf-additions")
                    .then(Commands.literal("get-status-checker").executes(context -> {
                                Entity entity = context.getSource().getEntity();
                                if (!(entity instanceof Player player)) {
                                    failed(context);
                                    return 0;
                                }

                                ItemStack statusChecker = new ItemStack(Items.WRITTEN_BOOK);
                                statusChecker.set(DataComponents.CUSTOM_NAME, Component.literal(HandbookHook.STATUS_CHECK));

                                VWUtil.addToInventory(player, statusChecker);
                                return 1;
                            }
                    )));
        });
    }

    private static void giveOrDropHandbook(ServerPlayer player, int toGive) {
        ItemStack mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
        AttachmentType<Integer> handbookType;
        String handbookName;

        ItemStack handbookItem = new ItemStack(Items.WRITTEN_BOOK);

        switch (toGive) {
            case 0 -> {
                handbookItem.set(DataComponents.CUSTOM_NAME, Component.literal(HandbookHook.ENCHANTMENTS_HANDBOOK_TITLE));
                handbookType = PlayerAttachment.RECEIVED_ENCHANTMENTS_HANDBOOK;
                handbookName = "Enchantments";
            }
            case 1 -> {
                handbookItem.set(DataComponents.CUSTOM_NAME, Component.literal(HandbookHook.EFFECTS_HANDBOOK_TITLE));
                handbookType = PlayerAttachment.RECEIVED_EFFECTS_HANDBOOK;
                handbookName = "Effects";
            }
            case 2 -> {
                handbookItem.set(DataComponents.CUSTOM_NAME, Component.translatable(HandbookHook.FEATURES_HANDBOOK_TITLE));
                handbookType = PlayerAttachment.RECEIVED_FEATURES_HANDBOOK;
                handbookName = "Features";
            }
            default -> {
                return;
            }
        }

        Inventory inv = player.getInventory();
        boolean hasItem = inv.contains(handbookItem);
        if (hasItem) {
            sendToChat(player, false, "You already have the " + handbookName + " Handbook!");
        } else if (mainHand.isEmpty()) {
            player.setItemSlot(EquipmentSlot.MAINHAND, handbookItem);
        } else {
            int slot = inv.getFreeSlot();
            if (slot < 1) {
                player.spawnAtLocation(player.level(), handbookItem);
            } else {
                inv.add(slot, handbookItem);
            }
        }

        player.setAttached(handbookType, 1);
    }
    private static ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) {
        ServerPlayer player;
        try {
            player = context.getSource().getPlayerOrException();
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("This command must be run by a player."));
            return null;
        }
        return player;
    }
    private static ServerPlayer getPlayer(CommandSourceStack source) {
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return null;
        }
        return player;
    }
    private static void handbookCopy(CommandContext<CommandSourceStack> c) {
        sendSuccess(c, false, "You can request another copy after your next respawn.");
    }
    private static void sendSuccess(CommandContext<CommandSourceStack> c, boolean broadcast, String msg) {
        c.getSource().sendSuccess(() -> Component.literal(msg), broadcast);
    }
    private static void sendSuccess(CommandSourceStack s, boolean broadcast, String msg) {
        s.sendSuccess(() -> Component.literal(msg), broadcast);
    }
    private static void sendFail(CommandContext<CommandSourceStack> c, String msg) {
        c.getSource().sendFailure(Component.literal(msg));
    }
    private static void sendFail(CommandContext<CommandSourceStack> c, int color, String msg) {
        c.getSource().sendFailure(Component.literal(msg).withColor(color));
    }
    private static void failed(CommandContext<CommandSourceStack> c) {
        failed(c.getSource());
    }
    private static void failed(CommandSourceStack c) {
        c.sendFailure(Component.literal("Failed to execute command."));
    }
}