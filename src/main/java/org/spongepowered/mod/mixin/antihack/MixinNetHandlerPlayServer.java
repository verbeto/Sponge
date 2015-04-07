package org.spongepowered.mod.mixin.antihack;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@NonnullByDefault
@Mixin(net.minecraft.network.NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer {

    @Shadow
    public EntityPlayerMP playerEntity;

    @Inject(method="processPlayer(Lnet/minecraft/network/play/client/C03PacketPlayer;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;onGround:Z", opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER))
    public void enforceOnGround(C03PacketPlayer packetIn, CallbackInfo ci) {
        this.playerEntity.onGround = this.playerEntity.isCollidedVertically && packetIn.getPositionY() < 0.0;
        System.out.println(this.playerEntity.onGround);
    }
}
