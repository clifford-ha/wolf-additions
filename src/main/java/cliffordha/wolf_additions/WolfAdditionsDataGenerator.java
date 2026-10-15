package cliffordha.wolf_additions;

import cliffordha.wolf_additions.datagen.*;
import cliffordha.wolf_additions.datagen.villager.VWVillagerTrades;
import cliffordha.wolf_additions.tag.*;
import cliffordha.wolf_additions.registry.VWEnchantments;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class WolfAdditionsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(VWItemTags::new);
		pack.addProvider(VWBiomeTags::new);
		pack.addProvider(VWEnchantmentTags::new);
		pack.addProvider(VWDamageTypeTags::new);
		pack.addProvider(VWVillagerTradeTags::new);

		pack.addProvider(VWSoundsProvider::new);
		pack.addProvider(VWEngLangProvider::new);
		pack.addProvider(VWRegistryProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.ENCHANTMENT, VWEnchantments::bootstrap);
		registryBuilder.add(Registries.DAMAGE_TYPE, VWDamageTypes::bootstrap);

		registryBuilder.add(Registries.VILLAGER_TRADE, VWVillagerTrades::bootstrap);
	}
}