package cliffordha.wolf_additions.loot;

import cliffordha.wolf_additions.TOTVW;
import cliffordha.wolf_additions.registry.VWEnchantments;
import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class VWLootTables {
    private static final NumberProvider ONE_ROLL = ConstantValue.exactly(1);

    private static void modifyLootTables(ResourceKey<LootTable> key, FabricLootTableBuilder builder, LootTableSource source, HolderLookup.Provider provider) {
        if (BuiltInLootTables.ANCIENT_CITY.equals(key) || BuiltInLootTables.BURIED_TREASURE.equals(key)) {
            LootPool.Builder benedictionEnchantment = addEnchantedBookChance(provider, VWEnchantments.BENEDICTION_OF_THE_VERDANT_MOUNTAINS, 1, 0.05f);
            builder.pool(benedictionEnchantment.build());
        }
        if (BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE.equals(key)) {
            LootPool.Builder witheringEnch = addEnchantedBookChance(provider, VWEnchantments.WOLF_EFFECT_WITHERING, 1, 3, 0.1f);
            LootPool.Builder poisoningEnch = addEnchantedBookChance(provider, VWEnchantments.WOLF_EFFECT_POISONING, 3, 5, 0.1f);
            LootPool.Builder mightEnch = addEnchantedBookChance(provider, VWEnchantments.WOLF_EFFECT_MIGHT, 3, 5, 0.1f);

            builder.pool(witheringEnch.build()).pool(poisoningEnch.build()).pool(mightEnch.build());
        }
    }

    private static LootPool.Builder addEnchantedBookChance(HolderLookup.Provider provider, ResourceKey<Enchantment> ench, int lvl, float chance) {
        return LootPool.lootPool()
                .setRolls(ONE_ROLL)
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder()
                        .withEnchantment(provider.getOrThrow(ench), ConstantValue.exactly(lvl)))
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).build());
    }
    private static LootPool.Builder addEnchantedBookChance(HolderLookup.Provider provider, ResourceKey<Enchantment> ench, int lvlA, int lvlB, float chance) {
        return LootPool.lootPool()
                .setRolls(ONE_ROLL)
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).apply(new SetEnchantmentsFunction.Builder()
                        .withEnchantment(provider.getOrThrow(ench), UniformGenerator.between(lvlA, lvlB)))
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))).build());
    }

    public static void registerModifiers() {
        LootTableEvents.MODIFY.register(VWLootTables::modifyLootTables);
    }
    private static ResourceKey<LootTable> createFromPath(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(TOTVW.MOD_ID, path));
    }
    private static ResourceKey<LootTable> createFromDefault(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace(path));
    }
}
