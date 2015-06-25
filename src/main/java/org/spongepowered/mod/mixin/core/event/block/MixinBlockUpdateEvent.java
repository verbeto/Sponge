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
package org.spongepowered.mod.mixin.core.event.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.block.BlockUpdateEvent;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.interfaces.block.IMixinBlock;
import org.spongepowered.common.registry.SpongeGameRegistry;
import org.spongepowered.mod.interfaces.IMixinEvent;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@NonnullByDefault
@Mixin(value = BlockEvent.NeighborNotifyEvent.class, remap = false)
public abstract class MixinBlockUpdateEvent extends BlockEvent implements BlockUpdateEvent {

    @Shadow
    private final EnumSet<EnumFacing> notifiedSides;

    private Set<Location> affectedBlocks;

    public MixinBlockUpdateEvent(World world, BlockPos pos, IBlockState state, EnumSet<EnumFacing> notifiedSides) {
        super(world, pos, state);

        this.notifiedSides = notifiedSides;
    }

    @Override
    public Collection<Location> getAffectedBlocks() {
        if (this.affectedBlocks == null) {
            this.affectedBlocks = new HashSet<Location>();
            for (EnumFacing notifiedSide : this.notifiedSides) {
                BlockPos offset = this.pos.offset(notifiedSide);
                this.affectedBlocks.add(((org.spongepowered.api.world.World) this.world).getFullBlock(offset.getX(), offset.getY(), offset.getZ()));
            }
        }
        return this.affectedBlocks;
    }

    private static NeighborNotifyEvent fromSpongeEvent(BlockUpdateEvent blockUpdateEvent) {
        Location location = blockUpdateEvent.getBlock();
        SpongeGameRegistry registry = Sponge.getSpongeRegistry();

        EnumSet<EnumFacing> facings = EnumSet.noneOf(EnumFacing.class);
        for (Direction direction : Direction.values()) {
            if ((direction.isCardinal() || direction == Direction.UP || direction == Direction.DOWN) && blockUpdateEvent.getAffectedBlocks().contains(location.getRelative(direction))) {
                facings.add(registry.directionMap.get(direction));
            }
        }

        NeighborNotifyEvent
                event =
                new NeighborNotifyEvent((World) (Object) blockUpdateEvent.getBlock().getExtent(),
                                        new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()),
                                        (IBlockState) (Object) blockUpdateEvent.getBlock().getExtent()
                                                .getBlock(blockUpdateEvent.getBlock().getBlockPosition()), facings);
        ((IMixinEvent) event).setSpongeEvent(blockUpdateEvent);
        return event;
    }
}
