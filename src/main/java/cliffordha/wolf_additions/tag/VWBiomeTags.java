package cliffordha.wolf_additions.tag;

import cliffordha.wolf_additions.TOTVW;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static cliffordha.wolf_additions.tag.VWTagHelpers.biome;

public class VWBiomeTags extends FabricTagsProvider<Biome> {
    public VWBiomeTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.BIOME, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {

        getOrCreateRawBuilder(FOREST_WHERE_WOLVES_HOWL)
                .add(biome(Biomes.FOREST))
                .add(biome(Biomes.FLOWER_FOREST))
                .add(biome(Biomes.DARK_FOREST));
    }
    public static final TagKey<Biome> FOREST_WHERE_WOLVES_HOWL = create("forest_where_wolves_howl");

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, TOTVW.registerID(name)); }
}
