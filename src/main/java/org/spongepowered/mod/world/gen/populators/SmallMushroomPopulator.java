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

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;

import java.util.Random;

public class SmallMushroomPopulator extends SpongePopulator {

    private int mushroomsPerChunk;
    private WorldGenerator mushroomBrownGen;
    private WorldGenerator mushroomRedGen;

    public SmallMushroomPopulator(int mushroomsPerChunk) {
        this.mushroomsPerChunk = mushroomsPerChunk;
        this.mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, SHROOM);
        int j, k, l;
        BlockPos blockpos;
        for (j = 0; doGen && j < this.mushroomsPerChunk; ++j) {
            if (randomGenerator.nextInt(4) == 0) {
                k = randomGenerator.nextInt(16) + 8;
                l = randomGenerator.nextInt(16) + 8;
                // If I had to guess I would say the the following line is supposed to be
                // int i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() * 2);
                // Not changing it for consistency with vanilla, but something to check for fixes against in the future
                BlockPos blockpos2 = currentWorld.getHeight(pos.add(k, 0, l));
                this.mushroomBrownGen.generate(currentWorld, randomGenerator, blockpos2);
            }

            if (randomGenerator.nextInt(8) == 0) {
                k = randomGenerator.nextInt(16) + 8;
                l = randomGenerator.nextInt(16) + 8;
                int i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() * 2);
                blockpos = pos.add(k, i1, l);
                this.mushroomRedGen.generate(currentWorld, randomGenerator, blockpos);
            }
        }

        if (doGen && randomGenerator.nextInt(4) == 0) {
            j = randomGenerator.nextInt(16) + 8;
            k = randomGenerator.nextInt(16) + 8;
            l = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(j, 0, k)).getY() * 2);
            this.mushroomBrownGen.generate(currentWorld, randomGenerator, pos.add(j, l, k));
        }

        if (doGen && randomGenerator.nextInt(8) == 0) {
            j = randomGenerator.nextInt(16) + 8;
            k = randomGenerator.nextInt(16) + 8;
            l = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(j, 0, k)).getY() * 2);
            this.mushroomRedGen.generate(currentWorld, randomGenerator, pos.add(j, l, k));
        }
    }

}
