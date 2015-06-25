/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.mixin.core.fml.common.eventhandler;

import com.google.common.base.Optional;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.CauseTracked;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.event.callback.CallbackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.mod.SpongeMod;
import org.spongepowered.mod.interfaces.IMixinEvent;

import java.lang.reflect.InvocationTargetException;

@NonnullByDefault
@Mixin(value = net.minecraftforge.fml.common.eventhandler.Event.class, remap = false)
public abstract class MixinEvent implements CauseTracked, Cancellable, IMixinEvent {

    @Shadow public abstract void setCanceled(boolean cancel);
    @Shadow public abstract boolean isCanceled();

    protected Event spongeEvent;

    public Game getGame() {
        return SpongeMod.instance.getGame();
    }

    @Override
    public Optional<Cause> getCause() {
        return Optional.fromNullable(new Cause(null, Optional.absent(), null));
    }

    @Override
    public boolean isCancelled() {
        if (spongeEvent instanceof Cancellable) {
            return ((Cancellable) spongeEvent).isCancelled();
        }
        return isCanceled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        if (spongeEvent instanceof Cancellable) {
            ((Cancellable) spongeEvent).setCancelled(cancel);
        }
        setCanceled(cancel);
    }

    @Override
    public void setSpongeEvent(Event spongeEvent) {
        this.spongeEvent = spongeEvent;
    }

    public CallbackList getCallbacks() {
        // TODO Implement callbacks
        return new CallbackList();
    }
}
