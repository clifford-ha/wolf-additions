package cliffordha.wolf_additions.mixin;

import cliffordha.wolf_additions.config.VWConfig;
import cliffordha.wolf_additions.entity.skills.VerdantWindBlessing;
import cliffordha.wolf_additions.registry.attachments.VWAttachments;
import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import cliffordha.wolf_additions.registry.attachments.entity.WolfAttachment;
import cliffordha.wolf_additions.registry.*;
import cliffordha.wolf_additions.tag.VWItemTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

import static cliffordha.wolf_additions.registry.VWEnchantments.*;
import static cliffordha.wolf_additions.util.VWUtil.*;

@Mixin(Wolf.class)
public abstract class WolfEntityMixin extends LivingEntity {
    protected WolfEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Unique
    private static void setAttributeBaseValue(Wolf wolf, Holder<Attribute> attribute, double amount) {
        if (wolf.getAttributes().hasAttribute(attribute)) {
            wolf.getAttribute(attribute).setBaseValue(amount);
        }
    }

    @Inject(method = "canMate", at = @At("HEAD"), cancellable = true)
    private void canMate(Animal partner, CallbackInfoReturnable<Boolean> cir) {
        Wolf wolf = (Wolf) (Object) this;

        UUID wolfID = wolf.getAttachedOrElse(WolfAttachment.FAMILY_ID, wolf.getUUID());
        UUID partnerID = partner.getAttachedOrElse(WolfAttachment.FAMILY_ID, partner.getUUID());

        cir.setReturnValue(!wolfID.equals(partnerID));
    }

    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/wolf/Wolf;", at = @At("RETURN"), cancellable = true)
    private void inheritStat(ServerLevel level, AgeableMob partner, CallbackInfoReturnable<Wolf> cir) {
        Wolf baby = EntityTypes.WOLF.create(level, EntitySpawnReason.BREEDING);
        Wolf wolf = (Wolf) (Object) this;

        if (baby != null && partner instanceof Wolf wolfPartner) {
            baby.setAttached(WolfAttachment.SOUL_ID, UUID.randomUUID());

            UUID value;

            UUID familyIDWolf = wolf.getAttachedOrElse(WolfAttachment.FAMILY_ID, null);
            UUID familyIDPartner = wolfPartner.getAttachedOrElse(WolfAttachment.FAMILY_ID, null);

            if (familyIDWolf != null && familyIDPartner != null) {
                value = level.getRandom().nextBoolean() ? familyIDWolf : familyIDPartner;
                baby.setAttached(WolfAttachment.FAMILY_ID, value);

            } else if (familyIDWolf != null && familyIDPartner == null) {
                baby.setAttached(WolfAttachment.FAMILY_ID, familyIDWolf);
                wolfPartner.setAttached(WolfAttachment.FAMILY_ID, familyIDWolf);

            } else if (familyIDPartner != null && familyIDWolf == null) {
                baby.setAttached(WolfAttachment.FAMILY_ID, familyIDPartner);
                wolf.setAttached(WolfAttachment.FAMILY_ID, familyIDPartner);

            } else {
                UUID wolfID = wolf.getAttachedOrElse(WolfAttachment.SOUL_ID, wolf.getUUID());
                UUID partnerID = wolfPartner.getAttachedOrElse(WolfAttachment.SOUL_ID, wolfPartner.getUUID());

                value = level.getRandom().nextBoolean() ?
                        new UUID(wolfID.getMostSignificantBits(), partnerID.getLeastSignificantBits()):
                        new UUID(partnerID.getMostSignificantBits(), wolfID.getLeastSignificantBits());

                wolfPartner.setAttached(WolfAttachment.FAMILY_ID, value);
                wolf.setAttached(WolfAttachment.FAMILY_ID, value);
                baby.setAttached(WolfAttachment.FAMILY_ID, value);
            }

            if (this.random.nextBoolean()) {
                baby.setVariant(wolf.getVariant());
            } else {
                baby.setVariant(wolfPartner.getVariant());
            }

            if (wolf.isTame()) {
                baby.setOwnerReference(wolf.getOwnerReference());
                baby.setTame(true, true);
                DyeColor parent1CollarColor = wolf.getCollarColor();
                DyeColor parent2CollarColor = wolfPartner.getCollarColor();
                baby.setCollarColor(DyeColor.getMixedColor(level, parent1CollarColor, parent2CollarColor));
            }

            baby.setSoundVariant(WolfSoundVariants.pickRandomSoundVariant(this.registryAccess(), this.random));

            cir.setReturnValue(baby);
        }
    }
    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void setSpawnData(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        Wolf wolf = (Wolf) (Object) this;

        if (spawnReason != EntitySpawnReason.LOAD) {
            wolf.setAttached(WolfAttachment.SOUL_ID, UUID.randomUUID());
        }
    }

    @Inject(method = "applyTamingSideEffects", at = @At("HEAD"), cancellable = true)
    private void createAttributes(CallbackInfo ci) {
        Wolf wolf = (Wolf) (Object) this;
        if (wolf.isTame()) {
            setAttributeBaseValue(wolf, Attributes.MAX_HEALTH, 40.0);
            wolf.setHealth(40.0f);
            if (wolf.getOwner() instanceof Player player) {
                UUID SHARED= UUID.randomUUID();
                player.setAttached(VWAttachments.WOLF_PLAYER_SHARED_ID, SHARED);
                wolf.setAttached(VWAttachments.WOLF_PLAYER_SHARED_ID, SHARED);
            }
        } else {
            setAttributeBaseValue(wolf, Attributes.MAX_HEALTH, 20.0);
        }
        ci.cancel();
    }
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void initializeAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(Animal.createAnimalAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.305)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.125)
                .add(Attributes.SCALE, 1.0)
        );
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Wolf wolf = (Wolf) (Object) this;
        Level level = wolf.level();

        if (wolf.isBaby() && wolf.isOnFire() && level.getGameTime() % 20 == 0) {
            List<Wolf> parents = level.getEntitiesOfClass(
                    Wolf.class,
                    scanArea(wolf, 16),
                    test -> WolfAttachment.isFamilyRelated(wolf, test)
                            && (VWEnchantments.getIgnition(test) > 0 || VWEnchantments.getMight(test) >= 3));

            if (!parents.isEmpty()) {
                wolf.getNavigation().moveTo(parents.getFirst(), 1.1);
                wolf.extinguishFire();
            }
        }
    }
    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void reviveWolf(DamageSource source, CallbackInfo ci) {
        Wolf wolf = (Wolf) (Object) this;
        if (!(wolf.level() instanceof ServerLevel level)) return;
        boolean shouldCancel = VerdantWindBlessing.reviveWolf(wolf, level, source);
        if (shouldCancel) {
            ci.cancel();
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Wolf wolf = (Wolf) (Object) this;
        Level getLevel = wolf.level();

        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(stack)) return;

        String name = wolf.getPlainTextName();
        boolean isTame = wolf.isTame();

        if (!(getLevel instanceof ServerLevel level)) return;

        if (!isTame && !wolf.isAngry()) {
            if (stack.is(Items.BONE)) {
                int atrocityCount = player.getAttachedOrElse(PlayerAttachment.WOLF_ATROCITY_COUNT, 0);
                if (atrocityCount < 20) {
                    consumeItem(player, stack);
                    if (level.getRandom().nextInt(3) == 0) {
                        wolf.tame(player);
                        wolf.getNavigation().stop();
                        wolf.setTarget(null);
                        wolf.setOrderedToSit(true);
                        level.broadcastEntityEvent(wolf, (byte) 7);
                    } else {
                        level.broadcastEntityEvent(wolf, (byte) 6);
                    }
                } else {
                    sendToChat(player, VWColors.DEFAULT_MUTED, false, "Something is preventing you from taming this wolf. Try again later.\nWolf atrocity count: " + atrocityCount);
                    cir.setReturnValue(InteractionResult.FAIL);
                }
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            } else if (stack.is(VWItemTags.WOLF_ARMOR_ENCHANTABLE) && !wolf.isWearingBodyArmor()) {
                wolf.equipItemIfPossible(level, stack);
                consumeItem(player, stack);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
        if (wolf.getOwner() == player) {
            if (stack.is(ItemTags.BEDS)) {
                BlockPos pos = player.getAttachedOrElse(PlayerAttachment.RESPAWN_POINT, player.blockPosition());
                if (pos.getX() == 0 && pos.getZ() == 0) {
                    sendToChat(player, true, "Cannot set a return point here");
                    cir.setReturnValue(InteractionResult.FAIL);
                } else {
                    wolf.setAttached(WolfAttachment.RESPAWN_POINT, pos);
                    sendToChat(player, VWColors.VERDANT_WIND, true, name + " has been set to return to " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
                    player.playSound(VWSounds.NOTIFY);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }

            }
        }

        if (stack.is(Items.TOTEM_OF_UNDYING) && VWEnchantments.getBenediction(wolf)) {
            int STACK = wolf.getAttachedOrElse(WolfAttachment.BENEDICTION, 0);
            int STACK_LIMIT = 3;

            if (STACK > STACK_LIMIT) {
                wolf.setAttached(WolfAttachment.BENEDICTION, STACK_LIMIT);
                cir.setReturnValue(InteractionResult.FAIL);
            }

            if (STACK < STACK_LIMIT) {
                wolf.setAttached(WolfAttachment.BENEDICTION, STACK + 1);
                level.playSound(null, wolf.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.NEUTRAL);
                sendParticles(VWParticles.BENEDICTION_TRIGGER_PARTICLE, level, wolf.blockPosition(), 12, 0.5);
                sendToChat(player, VWColors.VERDANT_WIND, true, wolf.getPlainTextName() + " Benediction stack " + wolf.getAttachedOrElse(WolfAttachment.BENEDICTION, 0));
                consumeItem(player, stack);
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                sendToChat(player, true, name + " already reached Benediction Stack limit!");
                cir.setReturnValue(InteractionResult.PASS);
            }
        }
    }

    @Inject(method = "wantsToAttack", at = @At("RETURN"), cancellable = true)
    private void wantsToAttack(LivingEntity target, LivingEntity owner, CallbackInfoReturnable<Boolean> cir) {
        Wolf wolf = (Wolf) (Object) this;
        boolean isPlayerOrWolf = target instanceof Player || target instanceof Wolf;
        if (isPlayerOrWolf && wolf.isTame()) {
            if (target instanceof Wolf wolfy && wolfy.isBaby()) {
                cir.setReturnValue(false);
                return;
            }
            cir.setReturnValue(WolfAttachment.isPlayerToBeAttacked(wolf, target));

        } else if (target instanceof Creeper) {
            if (wolf.getHealth() < wolf.getMaxHealth() * 0.5f) return;
            if (!wolf.isWearingBodyArmor()) return;
            int encProtection = wolfEnchantmentLVL(wolf, Enchantments.PROTECTION);
            int encBlastProtection = wolfEnchantmentLVL(wolf, Enchantments.BLAST_PROTECTION);
            int encGnawing = VWEnchantments.getGnawing(wolf);
            int encMight = VWEnchantments.getMight(wolf);
            boolean encBenediction = VWEnchantments.getBenediction(wolf);
            if (!(encGnawing > 0 && (encProtection >= 3 || encBlastProtection >= 3 || encMight > 2 || encBenediction))) return;
            cir.setReturnValue(true);

        }
    }


    @Inject(method = "canArmorAbsorb", at = @At("RETURN"), cancellable = true)
    private void absorbDMG(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        Wolf wolf = (Wolf) (Object) this;
        cir.setReturnValue(wolf.getBodyArmorItem().is(VWItemTags.WOLF_ARMOR_ENCHANTABLE) && !source.is(DamageTypeTags.BYPASSES_WOLF_ARMOR));
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"), cancellable = true)
    private void distributeHurt(ServerLevel level, DamageSource source, float damage, CallbackInfo ci) {
        Wolf wolf = (Wolf) (Object) this;

        ItemStack armor = wolf.getBodyArmorItem();
        if (!armor.isEmpty() || armor != null) {
            float computedDMG = VWEnchantments.getEnhancementKit(wolf) ? damage * 0.8f : damage;

            if (!VWConfig.get().SERVER_WOLF_DMG_DISTRIBUTION || !wolf.canArmorAbsorb(source)) {
                super.actuallyHurt(level, source, computedDMG);
                ci.cancel();
                return;
            }

            int finalArmorDMG = Mth.ceil(computedDMG * 0.75f);
            double finalWolfDMG = Math.floor(computedDMG * 0.25f);

            armor.hurtAndBreak(finalArmorDMG, wolf, EquipmentSlot.BODY);
            super.actuallyHurt(level, source, (float) finalWolfDMG);

            ci.cancel();
        }
    }

    @Unique
    private static void consumeItem(Player player, ItemStack itemStack) {
        if (player.isCreative()) return;
        itemStack.shrink(1);
    }
}
