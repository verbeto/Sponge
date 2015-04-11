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

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;

import java.util.Random;

public class SandPopulator extends SpongePopulator {

    private int sandPerChunk;
    private EventType type;
    public WorldGenerator sandGen;

    public SandPopulator(Block block, int sandPerChunk, int radius) {
        this.sandPerChunk = sandPerChunk;
        this.sandGen = new WorldGenSand(block, radius);
        if (block == Blocks.gravel) {
            this.type = EventType.SAND_PASS2;
        } else {
            this.type = EventType.SAND;
        }
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {
        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, this.type);
        for (int i = 0; doGen && i < this.sandPerChunk; ++i) {
            int j = randomGenerator.nextInt(16) + 8;
            int k = randomGenerator.nextInt(16) + 8;
            this.sandGen.generate(currentWorld, randomGenerator, currentWorld.getTopSolidOrLiquidBlock(pos.add(j, 0, k)));
        }
    }

}
