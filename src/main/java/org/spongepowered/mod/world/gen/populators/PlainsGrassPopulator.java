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
package org.spongepowered.mod.world.gen.populators;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.mod.interfaces.IBiomeGenPlains;

import java.util.Random;

public class PlainsGrassPopulator extends SpongePopulator {

    private static NoiseGeneratorPerlin NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);

    private WorldGenDoublePlant gen;

    public PlainsGrassPopulator() {
        this.gen = new WorldGenDoublePlant();
    }

    @Override
    protected void populate(World world, Chunk chunk, Random random, BlockPos pos) {
        BiomeGenBase biomegenbase = world.getBiomeGenForCoords(pos.add(16, 0, 16));
        double d0 = NOISE.func_151601_a((double) (pos.getX() + 8) / 200.0D, (double) (pos.getZ() + 8) / 200.0D);
        int i;
        int j;
        int k;
        int l;

        if (d0 >= -0.8D) {
            this.gen.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);

            for (i = 0; i < 7; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = safeNextInt(random, world.getHeight(pos.add(j, 0, k)).getY() + 32);
                this.gen.generate(world, random, pos.add(j, l, k));
            }
        }

        if (biomegenbase instanceof IBiomeGenPlains && ((IBiomeGenPlains) biomegenbase).hasSunflowers()) {
            this.gen.func_180710_a(BlockDoublePlant.EnumPlantType.SUNFLOWER);

            for (i = 0; i < 10; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = safeNextInt(random, world.getHeight(pos.add(j, 0, k)).getY() + 32);
                this.gen.generate(world, random, pos.add(j, l, k));
            }
        }
    }

}
