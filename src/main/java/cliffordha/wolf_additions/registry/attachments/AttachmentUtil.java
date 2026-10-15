package cliffordha.wolf_additions.registry.attachments;

import cliffordha.wolf_additions.TOTVW;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttachmentUtil {
    public static final UUID EMPTY_UUID = new UUID(0L, 0L);

    private static final Codec<Pair<String, UUID>> ENTITY_DATA_CODEC = RecordCodecBuilder.create(
            pair -> pair.group(
                    Codec.STRING.fieldOf("name").forGetter(Pair::getA),
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(Pair::getB)
            ).apply(pair, Pair::new)
    );
    private static final StreamCodec<FriendlyByteBuf, Pair<String, UUID>> ENTITY_DATA_CODEC_STREAM = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Pair::getA,
            UUIDUtil.STREAM_CODEC, Pair::getB,
            Pair::new
    );
    public static AttachmentType<List<Pair<String, UUID>>> registerListPair(String name, boolean copyOnDeath) {
        return AttachmentRegistry.create(
                TOTVW.registerID(name),
                builder -> {
                    builder.persistent(ENTITY_DATA_CODEC.listOf())
                            .syncWith(ENTITY_DATA_CODEC_STREAM.apply(ByteBufCodecs.list()), AttachmentSyncPredicate.all())
                            .initializer(ArrayList::new);
                    if (copyOnDeath) builder.copyOnDeath();
                }
        );
    }
    public static AttachmentType<UUID> registerUUID(String name, boolean copyOnDeath) {
        return AttachmentRegistry.create(
                TOTVW.registerID(name),
                builder -> {
                    builder.persistent(UUIDUtil.CODEC)
                            .syncWith(ByteBufCodecs.fromCodec(UUIDUtil.CODEC), AttachmentSyncPredicate.all())
                            .initializer(() -> EMPTY_UUID);
                    if (copyOnDeath) builder.copyOnDeath();
                }
        );
    }
    public static AttachmentType<BlockPos> registerBlockPos(String name, boolean copyOnDeath) {
        return AttachmentRegistry.create(
                TOTVW.registerID(name),
                builder -> {
                    builder.persistent(BlockPos.CODEC)
                            .syncWith(BlockPos.STREAM_CODEC, AttachmentSyncPredicate.all())
                            .initializer(() -> BlockPos.ZERO);
                    if (copyOnDeath) builder.copyOnDeath();
                }
        );
    }
    public static AttachmentType<Integer> registerInt(String name, boolean copyOnDeath) {
        return AttachmentRegistry.create(
                TOTVW.registerID(name),
                builder -> {
                    builder.persistent(Codec.INT)
                            .syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
                            .initializer(() -> 0);
                    if (copyOnDeath) builder.copyOnDeath();
                }
        );
    }
    public static AttachmentType<Boolean> registerBool(String name, boolean copyOnDeath) {
        return AttachmentRegistry.create(
                TOTVW.registerID(name),
                builder -> {
                    builder.persistent(Codec.BOOL)
                            .syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
                            .initializer(() -> false);
                    if (copyOnDeath) builder.copyOnDeath();
                }
        );
    }
    public static AttachmentType<Float> registerFloat(String name, boolean copyOnDeath) {
        return AttachmentRegistry.create(
                TOTVW.registerID(name),
                builder -> {
                    builder.persistent(Codec.FLOAT)
                            .syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all())
                            .initializer(() -> 0.0f);
                    if (copyOnDeath) builder.copyOnDeath();
                }
        );
    }
}
