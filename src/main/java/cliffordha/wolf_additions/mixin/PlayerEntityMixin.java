package cliffordha.wolf_additions.mixin;

import cliffordha.wolf_additions.registry.*;
import cliffordha.wolf_additions.registry.attachments.entity.PlayerAttachment;
import cliffordha.wolf_additions.registry.attachments.entity.WolfAttachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cliffordha.wolf_additions.util.VWUtil.sendToChat;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void onInteract(Entity entity, InteractionHand hand, Vec3 location, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = (Player) (Object) this;
        ItemStack itemStack = player.getItemInHand(hand);

        int atrocityCount = player.getAttachedOrElse(PlayerAttachment.VILLAGER_ATROCITY_COUNT, 0);
        if (atrocityCount > 20 && entity instanceof Villager || entity instanceof WanderingTrader) {
            sendToChat(player, true, "Your atrocity count (" + atrocityCount + ") is too high!");
            return;
        }
        
        String target = entity.getPlainTextName();
        if (entity instanceof Wolf wolf) {
            AttachmentType<Integer> BENEDICTION_STACK = WolfAttachment.BENEDICTION;

            if (itemStack.is(Items.STICK)) {
                if (player.isCrouching()) {
                    sendToChat(player, true, target + " has " + wolf.getAttachedOrElse(BENEDICTION_STACK, 0) + " remaining Benediction stack(s)");
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

    @Inject(method = "isSwimming", at = @At("RETURN"), cancellable = true)
    private void canSwim(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (player.hasEffect(VWEffects.PARALYZE) && !player.getAbilities().instabuild && !player.isSpectator()) cir.setReturnValue(false);
    }
}
