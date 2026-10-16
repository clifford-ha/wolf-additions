package cliffordha.wolf_additions.datagen.villager;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.registry.VWEnchantments;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import java.util.Optional;

import static net.minecraft.world.item.trading.VillagerTrades.enchantedBook;

public class VWVillagerTrades {
    public static final ResourceKey<VillagerTrade> WEAPONSMITH_2_WOLF_ATK_ENCHANTMENTS = createKey("weaponsmith/2/wolf_atk_enchantments");
    public static final ResourceKey<VillagerTrade> LIBRARIAN_2_WOLF_ATK_ENCHANTMENTS = createKey("librarian/2/wolf_atk_enchantments");
    public static final ResourceKey<VillagerTrade> ARMORER_1_WOLF_ENHANCEMENT_KIT = createKey("armorer/1/wolf_enhancement_kit");
    public static final ResourceKey<VillagerTrade> WANDERING_FAR_AWAY_ENCHANTMENTS = createKey("wandering_trader/far_away_enchantments");


    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);

        context.register(WEAPONSMITH_2_WOLF_ATK_ENCHANTMENTS, new VillagerTrade(
                new TradeCost(Items.EMERALD, 24),
                Optional.of(new TradeCost(Items.FIRE_CHARGE, 16)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(3),
                exact(50),
                defaultFloat(),
                Optional.empty(),
                enchantedBook(items, HolderSet.direct(
                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_IGNITION)
                )),
                HolderSet.empty()
        ));


        context.register(LIBRARIAN_2_WOLF_ATK_ENCHANTMENTS, new VillagerTrade(
                new TradeCost(Items.EMERALD, 24),
                Optional.of(new TradeCost(Items.BOOK, 16)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(6),
                exact(20),
                defaultFloat(),
                Optional.empty(),
                enchantedBook(items, HolderSet.direct(
                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_LIFTING),
                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_MIGHT),
                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_OOZING)
                )),
                HolderSet.empty()
        ));


        context.register(ARMORER_1_WOLF_ENHANCEMENT_KIT, new VillagerTrade(
                new TradeCost(Items.EMERALD, 16),
                Optional.of(new TradeCost(Items.IRON_INGOT, 10)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(2),
                exact(15),
                defaultFloat(),
                Optional.empty(),
                enchantedBook(items, HolderSet.direct(
                        enchantments.getOrThrow(VWEnchantments.WOLF_ARMOR_ENHANCEMENT_KIT)
                )),
                HolderSet.empty()
        ));
        context.register(WANDERING_FAR_AWAY_ENCHANTMENTS, new VillagerTrade(
                new TradeCost(Items.EMERALD, 10),
                Optional.of(new TradeCost(Items.BOOK, 12)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(2),
                exact(50),
                defaultFloat(),
                Optional.empty(),
                enchantedBook(items, HolderSet.direct(
                        enchantments.getOrThrow(VWEnchantments.BENEDICTION_OF_THE_VERDANT_MOUNTAINS),
                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_OOZING)
                )),
                HolderSet.empty()
        ));
    }


    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, TOTVW.registerID(name));
    }
    private static int exact(int value) {
        return Math.min(value, 64);
    }
    private static float exact(float value) {
        return Math.min(value, 1.0f);
    }
    private static float defaultFloat() {
        return 0.05f;
    }
}
