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
package org.spongepowered.mod.util.gen;

import com.google.common.collect.Maps;

import net.minecraft.world.gen.NoiseGeneratorPerlin;
import org.spongepowered.api.util.VariableAmount;

import java.util.Map;
import java.util.Random;

public class NoiseVariableAmount extends VariableAmount {

    private static Map<Long, NoiseGeneratorPerlin> NOISE_GENS = Maps.newHashMap();

    private static NoiseGeneratorPerlin getOrCreateNoiseForSeed(long seed) {
        if (NOISE_GENS.containsKey(seed)) {
            return NOISE_GENS.get(seed);
        }
        Random rand = new Random(seed);
        NoiseGeneratorPerlin noise = new NoiseGeneratorPerlin(rand, 4);
        NOISE_GENS.put(seed, noise);
        return noise;
    }

    private final NoiseGeneratorPerlin noise;
    private double[] stoneNoise;
    private int lastX;
    private int lastZ;

    public NoiseVariableAmount(long seed) {
        this.noise = getOrCreateNoiseForSeed(seed);
    }

    private double getStoneNoise(int x, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        if (this.stoneNoise == null || this.lastX != chunkX || this.lastZ != chunkZ) {
            this.stoneNoise =
                    this.noise.func_151599_a(this.stoneNoise, (double) (chunkX * 16), (double) (chunkZ * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);
        }
        return this.stoneNoise[(x % 16) + (z % 16) * 16];
    }

    @Override
    public double getValue(Random rand, int x, int z) {
        double sn = getStoneNoise(x, z);
        return sn / 3.0D + 3.0D + rand.nextDouble() * 0.25D;
    }

}
