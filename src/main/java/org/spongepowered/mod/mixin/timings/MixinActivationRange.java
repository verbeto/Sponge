package org.spongepowered.mod.mixin.timings;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.mod.mixin.plugin.entityactivation.ActivationRange;
import org.spongepowered.mod.mixin.plugin.timings.SpongeTimings;

@NonnullByDefault
@Mixin(ActivationRange.class)
public class MixinActivationRange {

    @Inject(method = "activateEntities", at = @At("HEAD"))
    private static void onActivateEntitiesHead(World world, CallbackInfo ci) {
        SpongeTimings.entityActivationCheckTimer.startTiming();
    }

    @Inject(method = "activateEntities", at = @At("RETURN"))
    private static void onActivateEntitiesReturn(World world, CallbackInfo ci) {
        SpongeTimings.entityActivationCheckTimer.stopTiming();
    }

    @Inject(method = "checkIfActive", at = @At("HEAD"))
    private static void onCheckIfActiveHead(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        SpongeTimings.checkIfActiveTimer.startTiming();
    }

    @Inject(method = "checkIfActive", at = @At("RETURN"))
    private static void onCheckIfActiveReturn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        SpongeTimings.checkIfActiveTimer.stopTiming();
    }
}
