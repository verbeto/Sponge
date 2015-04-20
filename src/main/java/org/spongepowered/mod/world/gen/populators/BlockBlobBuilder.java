/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <http://www.spongepowered.org>
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
package org.spongepowered.mod.world.gen.populators;

import net.minecraft.block.Block;

import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import static com.google.common.base.Preconditions.checkNotNull;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.world.gen.populators.BlockBlob.Builder;
import org.spongepowered.api.world.gen.populators.BlockBlob;

public class BlockBlobBuilder implements BlockBlob.Builder {

    private BlockState block;
    private VariableAmount radius;
    private VariableAmount count;

    public BlockBlobBuilder() {
        reset();
    }

    @Override
    public Builder block(BlockState block) {
        checkNotNull(block, "block");
        this.block = block;
        return this;
    }

    @Override
    public Builder radius(VariableAmount radius) {
        checkNotNull(radius, "radius");
        this.radius = radius;
        return this;
    }

    @Override
    public Builder blockCount(VariableAmount count) {
        checkNotNull(count, "count");
        this.count = count;
        return this;
    }

    @Override
    public Builder reset() {
        this.count = VariableAmount.fixed(3);
        this.radius = VariableAmount.baseAndVariance(1, 2);
        this.block = null;
        return this;
    }

    @Override
    public BlockBlob build() throws IllegalStateException {
        if(this.block == null || this.radius == null || this.count == null) {
            throw new IllegalStateException("Missing required arguments for builder.");
        }
        BlockBlob blob = (BlockBlob) new WorldGenBlockBlob((Block) this.block.getType(), 1);
        blob.setBlock(this.block);
        blob.setCount(this.count);
        blob.setRadius(this.radius);
        return blob;
    }

}
