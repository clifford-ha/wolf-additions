package cliffordha.wolf_additions.tag;

import cliffordha.wolf_additions.datagen.villager.VWVillagerTrades;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.concurrent.CompletableFuture;

import static cliffordha.wolf_additions.tag.VWTagHelpers.trade;

public class VWVillagerTradeTags extends FabricTagsProvider<VillagerTrade> {
    public VWVillagerTradeTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.VILLAGER_TRADE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(VillagerTradeTags.WEAPONSMITH_LEVEL_2)
                .add(trade(VWVillagerTrades.WEAPONSMITH_2_WOLF_ATK_ENCHANTMENTS));

        getOrCreateRawBuilder(VillagerTradeTags.LIBRARIAN_LEVEL_2)
                .add(trade(VWVillagerTrades.LIBRARIAN_2_WOLF_ATK_ENCHANTMENTS));

        getOrCreateRawBuilder(VillagerTradeTags.ARMORER_LEVEL_1)
                .add(trade(VWVillagerTrades.ARMORER_1_WOLF_ENHANCEMENT_KIT));

        getOrCreateRawBuilder(VillagerTradeTags.WANDERING_TRADER_UNCOMMON)
                .add(trade(VWVillagerTrades.WANDERING_FAR_AWAY_ENCHANTMENTS));
    }
}
