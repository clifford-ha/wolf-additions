package cliffordha.wolf_additions.entity;

import net.minecraft.world.entity.ai.goal.GoalSelector;

@FunctionalInterface
public interface VWEntityAccessor {
    GoalSelector goalSelector();
}
