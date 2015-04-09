/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
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
package org.spongepowered.mod.world.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.biome.GroundCoverLayer;

public class DeferredGroundCoverLayer extends GroundCoverLayer {

    private final int index;
    private final boolean deferredDepth;
    private final BiomeGenBase biome;

    public DeferredGroundCoverLayer(BiomeGenBase biome, IBlockState state, int index) {
        super((BlockState) state);
        this.biome = biome;
        this.index = index;
        this.deferredDepth = true;
    }

    public DeferredGroundCoverLayer(BiomeGenBase biome, IBlockState state, int index, int base) {
        super((BlockState) state, base);
        this.biome = biome;
        this.index = index;
        this.deferredDepth = false;
    }

    public DeferredGroundCoverLayer(BiomeGenBase biome, IBlockState state, int index, int base, int var) {
        super((BlockState) state, base, var);
        this.biome = biome;
        this.index = index;
        this.deferredDepth = false;
    }

    public BlockState getState() {
        if(this.index == 0) {
            return (BlockState) biome.topBlock;
        }
        return (BlockState) biome.fillerBlock;
    }

    public int getDepth(Random rand, double stoneNoise) {
        if (this.deferredDepth) {
            return (int) (stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        }
        return (int) (getBaseDepth() + rand.nextDouble() * getDepthVariance());
    }

}
