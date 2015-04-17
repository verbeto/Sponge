package org.spongepowered.mod.mixin.core.scoreboard;

import net.minecraft.scoreboard.GoalColor;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@NonnullByDefault
@Mixin(value = {GoalColor.class, ScoreDummyCriteria.class})
@Implements(@Interface(iface = Criterion.class, prefix = "criterion$"))
public abstract class MixinCriterion {

    @Shadow public abstract String getName();

    @Intrinsic
    public String criterion$getName() {
        return this.getName();
    }
}
