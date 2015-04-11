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
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Melons;

import java.util.Random;

public class MelonPopulator extends SpongePopulator implements Melons {

    private WorldGenerator gen;

    public MelonPopulator() {
        this.gen = new WorldGenMelon();
    }

    @Override
    protected void populate(World world, Chunk chunk, Random random, BlockPos pos) {

        int i = random.nextInt(16) + 8;
        int j = random.nextInt(16) + 8;
        int k = safeNextInt(random, world.getHeight(pos.add(i, 0, j)).getY() * 2);
        this.gen.generate(world, random, pos.add(i, k, j));
    }

    @Override
    public int getMelonsPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMelonsPerChunk(int count) {
        // TODO Auto-generated method stub
        
    }

}
