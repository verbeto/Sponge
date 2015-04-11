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

import net.minecraft.world.biome.BiomeGenBase;

import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.world.Chunk;

import java.util.Random;

public class RoofedForestPopulator extends SpongePopulator {

    @Override
    protected void populate(World world, Chunk chunk, Random random, BlockPos pos) {
        BiomeGenBase biomegenbase = world.getBiomeGenForCoords(pos.add(16, 0, 16));
        int i, j, k, l;
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 4; ++j) {
                k = i * 4 + 1 + 8 + random.nextInt(3);
                l = j * 4 + 1 + 8 + random.nextInt(3);
                BlockPos blockpos1 = world.getHeight(pos.add(k, 0, l));

                if (random.nextInt(20) == 0) {
                    WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
                    worldgenbigmushroom.generate(world, random, blockpos1);
                } else {
                    WorldGenAbstractTree worldgenabstracttree = biomegenbase.genBigTreeChance(random);
                    worldgenabstracttree.func_175904_e();

                    if (worldgenabstracttree.generate(world, random, blockpos1)) {
                        worldgenabstracttree.func_180711_a(world, random, blockpos1);
                    }
                }
            }
        }
    }

}
