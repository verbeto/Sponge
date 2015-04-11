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

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.REED;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Reeds;

import java.util.Random;

public class ReedPopulator extends SpongePopulator implements Reeds {

    private int reedsPerChunk;
    private WorldGenerator gen;

    public ReedPopulator(int reedsPerChunk) {
        this.reedsPerChunk = reedsPerChunk + 10;
        this.gen = new WorldGenReed();
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {

        int j, k, l, i1;

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, REED);
        for (j = 0; doGen && j < this.reedsPerChunk; ++j) {
            k = randomGenerator.nextInt(16) + 8;
            l = randomGenerator.nextInt(16) + 8;
            i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() * 2);
            this.gen.generate(currentWorld, randomGenerator, pos.add(k, i1, l));
        }
    }

    @Override
    public int getReedsPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setReedsPerChunk(int count) {
        // TODO Auto-generated method stub
        
    }

}
