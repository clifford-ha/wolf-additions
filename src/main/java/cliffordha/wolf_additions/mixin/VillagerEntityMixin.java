package cliffordha.wolf_additions.mixin;

import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class VillagerEntityMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void onInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Villager villager = (Villager) (Object) this;
        if (player.getAttachedOrElse(PlayerAttachment.VILLAGER_ATROCITY_COUNT, 0) > 20) {
            if (!villager.level().isClientSide()) {
                villager.makeSound(SoundEvents.VILLAGER_NO);
            }
            int unhappiness = villager.getUnhappyCounter();
            villager.setUnhappyCounter(unhappiness + 12);
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}