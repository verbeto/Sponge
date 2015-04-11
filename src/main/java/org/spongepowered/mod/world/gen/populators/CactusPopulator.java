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

import static com.google.common.base.Preconditions.checkArgument;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CACTUS;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Cactus;

import java.util.Random;

public class CactusPopulator extends SpongePopulator implements Cactus {

    private WorldGenerator gen;
    private int cactiPerChunk;

    public CactusPopulator(int cactiPerChunk) {
        this.gen = new WorldGenCactus();
        this.cactiPerChunk = cactiPerChunk;
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {
        int j, k, l, i1;
        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, CACTUS);
        for (j = 0; doGen && j < this.cactiPerChunk; ++j) {
            k = randomGenerator.nextInt(16) + 8;
            l = randomGenerator.nextInt(16) + 8;
            i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() * 2);
            this.gen.generate(currentWorld, randomGenerator, pos.add(k, i1, l));
        }
    }

    @Override
    public int getCactiPerChunk() {
        return this.cactiPerChunk;
    }

    @Override
    public void setCactiPerChunk(int count) {
        checkArgument(count >= 0, "Count of cacti per chunk for cactus populator cannot be negative");
        this.cactiPerChunk = count;
    }

}
