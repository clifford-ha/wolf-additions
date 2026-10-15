package cliffordha.wolf_additions;

import cliffordha.wolf_additions.config.VWConfig;
import cliffordha.wolf_additions.entity.VWGlobalEntityBehaviors;
import cliffordha.wolf_additions.networking.ServerboundPackets;
import cliffordha.wolf_additions.networking.VWNetworking;
import cliffordha.wolf_additions.loot.VWLootTables;
import cliffordha.wolf_additions.registry.*;
import cliffordha.wolf_additions.registry.attachments.VWAttachments;

import net.fabricmc.api.ModInitializer;

public class WolfAdditions implements ModInitializer {
	public WolfAdditions() {}

	public static final boolean IN_DEVELOPMENT = false;

	@Override
	public void onInitialize() {
		TOTVW.sendStat(TOTVW.MOD_NAME_LONG + " (or TOTVW for short) started initializing...");
		VWGlobalEntityBehaviors.register();

		VWAttachments.register();
		VWEnchantments.register();

		VWEffects.register();
		VWPotions.register();
		VWPotionBrewing.register();
		VWParticles.register();
		VWSounds.register();

		VWCommands.register();
		VWLootTables.registerModifiers();

		VWNetworking.register();
		ServerboundPackets.register();

		VWConfig.load();
		VWConfig.save();
	}
}