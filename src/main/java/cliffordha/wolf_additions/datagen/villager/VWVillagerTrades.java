package cliffordha.wolf_additions.datagen.villager;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.registry.VWEnchantments;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.List;
import java.util.Optional;

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
                Optional.of(
                        enchantedBook(items,
                                HolderSet.direct(enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_IGNITION)))
                ),
                Optional.empty()
        ));


        context.register(LIBRARIAN_2_WOLF_ATK_ENCHANTMENTS, new VillagerTrade(
                new TradeCost(Items.EMERALD, 24),
                Optional.of(new TradeCost(Items.BOOK, 16)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(6),
                exact(20),
                defaultFloat(),
                Optional.empty(),
                Optional.of(
                        enchantedBook(items,
                                HolderSet.direct(
                                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_LIFTING),
                                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_MIGHT),
                                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_OOZING)
                                ))
                ),
                Optional.empty()
        ));


        context.register(ARMORER_1_WOLF_ENHANCEMENT_KIT, new VillagerTrade(
                new TradeCost(Items.EMERALD, 16),
                Optional.of(new TradeCost(Items.IRON_INGOT, 10)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(2),
                exact(15),
                defaultFloat(),
                Optional.empty(),
                Optional.of(
                        enchantedBook(items,
                                HolderSet.direct(
                                        enchantments.getOrThrow(VWEnchantments.WOLF_ARMOR_ENHANCEMENT_KIT)
                                ))
                ),
                Optional.empty()
        ));
        context.register(WANDERING_FAR_AWAY_ENCHANTMENTS, new VillagerTrade(
                new TradeCost(Items.EMERALD, 10),
                Optional.of(new TradeCost(Items.BOOK, 12)),
                new ItemStackTemplate(Items.ENCHANTED_BOOK),
                exact(2),
                exact(50),
                defaultFloat(),
                Optional.empty(),
                Optional.of(
                        enchantedBook(items,
                                HolderSet.direct(
                                        enchantments.getOrThrow(VWEnchantments.BENEDICTION_OF_THE_VERDANT_MOUNTAINS),
                                        enchantments.getOrThrow(VWEnchantments.WOLF_EFFECT_OOZING)
                                ))
                ),
                Optional.empty()
        ));
    }


    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, TOTVW.registerID(name));
    }
    private static Holder<ContextIntProvider> exact(int value) {
        return ContextIntProviders.exactly(value);
    }
    private static Holder<ContextFloatProvider> defaultFloat() {
        return ContextFloatProviders.exactly(0.05f);
    }

    public static Holder<LootItemFunction> enchantedBook(final HolderGetter<Item> items, final HolderSet<Enchantment> options) {
        ItemPredicate.Builder bookWithAnyEnchants = (new ItemPredicate.Builder()).of(items, Items.ENCHANTED_BOOK).withComponents(net.minecraft.advancements.predicates.DataComponentMatchers.Builder.components().partial(DataComponentPredicates.STORED_ENCHANTMENTS, EnchantmentsPredicate.storedEnchantments(List.of(new EnchantmentPredicate(Optional.empty(), MinMaxBounds.Ints.ANY)))).build());
        return discardItemIfItsNot((new EnchantRandomlyFunction.Builder()).withOptions(options).allowingIncompatibleEnchantments().includeAdditionalCostComponent(), bookWithAnyEnchants);
    }
    public static Holder<LootItemFunction> discardItemIfItsNot(final LootItemFunction.Builder function, final ItemPredicate.Builder preserveCondition) {
        return VillagerTrades.discardItemIfItsNot(preserveCondition);
    }
}
