package org.spongepowered.mod.mixin.core.scoreboard;

import static com.google.common.base.Preconditions.*;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@NonnullByDefault
@Mixin(ScoreObjective.class)
@Implements(@Interface(iface = Objective.class, prefix = "objective$"))
public abstract class MixinScoreObjective {

    @Shadow public String displayName;

    @Shadow public IScoreObjectiveCriteria objectiveCriteria;

    @Shadow public Scoreboard theScoreboard;

    @Shadow public IScoreObjectiveCriteria.EnumRenderType renderType;

    @Shadow public abstract String getName();

    @Intrinsic
    public String objective$getName() {
        return this.getName();
    }

    public Text objective$getDisplayName() {
        return Texts.fromLegacy(this.displayName);
    }

    public void objective$setDisplayName(Text displayName) {
        if (Texts.toPlain(displayName).length() > 32) {
            throw new IllegalArgumentException("displayName is greater than 32 characters!");
        }
        this.displayName = Texts.toLegacy(displayName);
        this.theScoreboard.func_96532_b((ScoreObjective) (Object) this);
    }

    public Criterion objective$getCriterion() {
        return (Criterion) this.objectiveCriteria;
    }

    public ObjectiveDisplayMode objective$getDisplayMode() {

    }

}
