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

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.IcePath;

import java.util.Random;

public class IcePathPopulator extends SpongePopulator implements IcePath {

    private int count;
    private WorldGenerator gen;

    public IcePathPopulator(int count, int radius) {
        this.count = count;
        this.gen = new WorldGenIcePath(radius);
    }

    @Override
    protected void populate(World world, Chunk chunk, Random random, BlockPos pos) {
        int i;
        int j;
        int k;

        for (i = 0; i < this.count; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            this.gen.generate(world, random, world.getHeight(pos.add(j, 0, k)));
        }
    }

    @Override
    public int getRadius() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRadius(int radius) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getSectionsPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setSectionsPerChunk(int sections) {
        // TODO Auto-generated method stub
        
    }

}
