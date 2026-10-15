package cliffordha.wolf_additions.tag;

import cliffordha.wolf_additions.TOTVW;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

import static cliffordha.wolf_additions.tag.VWTagHelpers.type;
import static cliffordha.wolf_additions.datagen.VWDamageTypes.*;

public class VWDamageTypeTags extends FabricTagsProvider<DamageType> {
    public VWDamageTypeTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.DAMAGE_TYPE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {

        getOrCreateRawBuilder(DamageTypeTags.NO_KNOCKBACK)
                .add(type(BLOODLUST))
                .add(type(BLEEDING));

        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_ARMOR)
                .add(type(BLOODLUST))
                .add(type(BLEEDING))
                .add(type(SCORCHING_HEAT));

        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_WOLF_ARMOR)
                .add(type(BLOODLUST))
                .add(type(BLEEDING));

        getOrCreateRawBuilder(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)
                .add(type(SCORCHING_HEAT));

        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_RESISTANCE)
                .add(type(BLEEDING))
                .add(type(SCORCHING_HEAT))
                .add(type(BLOODLUST));

        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_SHIELD)
                .add(type(SCORCHING_HEAT));

        getOrCreateRawBuilder(DamageTypeTags.BYPASSES_EFFECTS)
                .add(type(BLEEDING))
                .add(type(SCORCHING_HEAT));

        getOrCreateRawBuilder(BENEDICTION_CAN_REDUCE_HIGH_DAMAGE)
                .add(type(DamageTypes.SONIC_BOOM))
                .add(type(DamageTypes.WITHER_SKULL))
                .add(type(DamageTypes.DRAGON_BREATH));
    }

    public static final TagKey<DamageType> BENEDICTION_CAN_REDUCE_HIGH_DAMAGE = create("benediction_can_reduce_high_damage");
    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, TOTVW.registerID(name)); }
}
