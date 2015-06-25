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
package org.spongepowered.mod.mixin.core.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.spongepowered.api.event.entity.EntityConstructingEvent;
import org.spongepowered.api.event.entity.EntitySpawnEvent;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.mod.interfaces.IMixinEvent;

@NonnullByDefault
@Mixin(value = net.minecraftforge.event.entity.EntityJoinWorldEvent.class, remap = false)
public abstract class MixinEventEntityJoinWorld extends EntityEvent implements EntitySpawnEvent {

    public MixinEventEntityJoinWorld(Entity entity) {
        super(entity);
    }

    @Override
    public Location getLocation() {
        return getEntity().getLocation();
    }

    private static EntityJoinWorldEvent fromSpongeEvent(EntitySpawnEvent spongeEvent) {
        EntityJoinWorldEvent event = new EntityJoinWorldEvent((net.minecraft.entity.Entity) spongeEvent.getEntity(), (World) spongeEvent.getLocation().getExtent());
        ((IMixinEvent) event).setSpongeEvent(spongeEvent);
        return event;
    }
}
