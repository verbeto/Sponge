package org.spongepowered.mod.mixin.core.scoreboard;

import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;

@NonnullByDefault
@Mixin(Scoreboard.class)
public abstract class MixinScoreboard implements org.spongepowered.api.scoreboard.Scoreboard {

}
