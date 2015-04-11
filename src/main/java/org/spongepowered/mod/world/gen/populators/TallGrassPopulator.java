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

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;

import java.util.Random;

public class TallGrassPopulator extends SpongePopulator {

    private static NoiseGeneratorPerlin NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);

    private int grassPerChunk;

    public TallGrassPopulator(int grassPerChunk) {
        this.grassPerChunk = grassPerChunk;
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {
        BiomeGenBase biomegenbase = currentWorld.getBiomeGenForCoords(pos.add(16, 0, 16));
        double d0 = NOISE.func_151601_a((double) (pos.getX() + 8) / 200.0D, (double) (pos.getZ() + 8) / 200.0D);
        int mod = 1;
        if (biomegenbase instanceof BiomeGenPlains) {
            if (d0 >= -0.8D) {
                mod = 2;
            }
        }

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, GRASS);
        for (int j = 0; doGen && j < this.grassPerChunk * mod; ++j) {
            int k = randomGenerator.nextInt(16) + 8;
            int l = randomGenerator.nextInt(16) + 8;
            int i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() * 2);
            biomegenbase.getRandomWorldGenForGrass(randomGenerator).generate(currentWorld, randomGenerator, pos.add(k, i1, l));
        }
    }

}
