package cliffordha.wolf_additions.tag;

import cliffordha.wolf_additions.TOTVW;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static cliffordha.wolf_additions.tag.VWTagHelpers.item;

public class VWItemTags extends FabricTagsProvider.ItemTagsProvider {
    public VWItemTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {

        getOrCreateRawBuilder(BENEDICTION_ENCHANTMENT_USE_QUALIFIED_TOOLS)
                .addOptionalTag(ItemTags.SWORDS.location())
                .addOptionalTag(ItemTags.AXES.location())
                .addOptionalTag(ItemTags.PICKAXES.location())
                .addOptionalTag(ItemTags.SHOVELS.location())
                .addOptionalTag(ItemTags.HOES.location());

        getOrCreateRawBuilder(BENEDICTION_ENCHANTMENT_USE_QUALIFIED_ITEMS)
                .add(item(Items.GLOWSTONE_DUST));

        getOrCreateRawBuilder(WOLF_ARMOR_ENCHANTABLE)
                .add(item(Items.WOLF_ARMOR));

        getOrCreateRawBuilder(BENEDICTION_ENCHANTABLE)
                .add(item(Items.WOLF_ARMOR))
                .addOptionalTag(ItemTags.CHEST_ARMOR.location());

        getOrCreateRawBuilder(ItemTags.CHEST_ARMOR)
                .add(item(Items.WOLF_ARMOR));
    }

    public static final TagKey<Item> BENEDICTION_ENCHANTMENT_USE_QUALIFIED_ITEMS = create("benediction_enchantment_use_qualified_items");
    public static final TagKey<Item> BENEDICTION_ENCHANTMENT_USE_QUALIFIED_TOOLS = create("benediction_enchantment_use_qualified_tools");
    public static final TagKey<Item> WOLF_ARMOR_ENCHANTABLE = create("wolf_armor_enchantable");
    public static final TagKey<Item> BENEDICTION_ENCHANTABLE = create("benediction_enchantable");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, TOTVW.registerID(name)); }
}