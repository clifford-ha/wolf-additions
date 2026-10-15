package cliffordha.wolf_additions.entity.player;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface PlayerBehaviorAction {
    void execute(Player player, ServerLevel level);
}
