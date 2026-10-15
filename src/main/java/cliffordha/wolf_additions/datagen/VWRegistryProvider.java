package cliffordha.wolf_additions.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class VWRegistryProvider extends FabricDynamicRegistryProvider {
    public VWRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
        entries.addAll(registries.lookupOrThrow(Registries.VILLAGER_TRADE));
    }

    @Override
    public String getName() {
        return "TOTVW Dynamic Registries";
    }
}
