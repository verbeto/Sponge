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

import org.spongepowered.api.block.BlockState;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.RandomLiquids;

import java.util.Random;

public class LiquidsPopulator extends SpongePopulator implements RandomLiquids {

    @Override
    protected void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {
        BlockPos blockpos1;
        int i, j, k, l;

        //TODO split in two
        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, LAKE_WATER);
        for (i = 0; doGen && i < 50; ++i) {
            j = randomGenerator.nextInt(16) + 8;
            k = randomGenerator.nextInt(randomGenerator.nextInt(248) + 8);
            l = randomGenerator.nextInt(16) + 8;
            blockpos1 = pos.add(j, k, l);
            (new WorldGenLiquids(Blocks.flowing_water)).generate(currentWorld, randomGenerator, blockpos1);
        }

        doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, LAKE_LAVA);
        for (i = 0; doGen && i < 20; ++i) {
            j = randomGenerator.nextInt(16) + 8;
            k = randomGenerator.nextInt(randomGenerator.nextInt(248) + 8);
            l = randomGenerator.nextInt(16) + 8;
            blockpos1 = pos.add(j, k, l);
            (new WorldGenLiquids(Blocks.flowing_lava)).generate(currentWorld, randomGenerator, blockpos1);
        }

    }

    @Override
    public int getAttemptsPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setAttemptsPerChunk(int attempts) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public BlockState getLiquidType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLiquidType(BlockState liquid) {
        // TODO Auto-generated method stub
        
    }

}
