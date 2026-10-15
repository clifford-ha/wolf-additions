package cliffordha.wolf_additions.entity.player;

import cliffordha.wolf_additions.registry.VWEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;

import static cliffordha.wolf_additions.registry.VWEnchantments.entityEnchantmentLVL;

public record EntityEnchants(
        boolean benediction,

        //from vanilla
        int protection,
        int fireProtection,
        int projectileProtection,
        int blastProtection
) {
    public static EntityEnchants of(LivingEntity entity) {
        return new EntityEnchants(
                VWEnchantments.getBenediction(entity),

                entityEnchantmentLVL(entity, Enchantments.PROTECTION),
                entityEnchantmentLVL(entity, Enchantments.FIRE_PROTECTION),
                entityEnchantmentLVL(entity, Enchantments.PROJECTILE_PROTECTION),
                entityEnchantmentLVL(entity, Enchantments.BLAST_PROTECTION)
        );
    }
}
