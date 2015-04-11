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

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LILYPAD;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.WaterLily;

import java.util.Random;

public class WaterLilyPopulator extends SpongePopulator implements WaterLily {

    private int waterlilyPerChunk;
    private WorldGenerator gen;

    public WaterLilyPopulator(int waterlilyPerChunk) {
        this.waterlilyPerChunk = waterlilyPerChunk;
        this.gen = new WorldGenDeadBush();
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, LILYPAD);
        int j = 0;
        BlockPos blockpos;
        while (doGen && j < this.waterlilyPerChunk) {
            int k = randomGenerator.nextInt(16) + 8;
            int l = randomGenerator.nextInt(16) + 8;
            int i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() * 2);
            blockpos = pos.add(k, i1, l);

            while (true) {
                if (blockpos.getY() > 0) {
                    BlockPos blockpos3 = blockpos.down();

                    if (currentWorld.isAirBlock(blockpos3)) {
                        blockpos = blockpos3;
                        continue;
                    }
                }

                this.gen.generate(currentWorld, randomGenerator, blockpos);
                ++j;
                break;
            }
        }
    }

    @Override
    public int getWaterLilyPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setWaterLilyPerChunk(int count) {
        // TODO Auto-generated method stub
        
    }

}
