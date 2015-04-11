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

import org.spongepowered.api.world.gen.types.BiomeTreeType;
import org.spongepowered.api.world.gen.types.BiomeTreeTypes;

import net.minecraft.block.BlockPlanks;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import static com.google.common.base.Preconditions.checkArgument;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.HugeTree;

import java.util.Random;

public class BigTreePopulator extends SpongePopulator implements HugeTree {

    private int count;
    private BiomeTreeType type;
    private WorldGenAbstractTree gen;

    public BigTreePopulator(int count, BiomeTreeType type) {
        this.count = count;
        this.type = type;
        if (type == BiomeTreeTypes.OAK) {
            this.gen = new WorldGenBigTree(false);
        } else if (type == BiomeTreeTypes.TALL_TAIGA) {
            this.gen = new WorldGenMegaPineTree(false, true);
        } else if (type == BiomeTreeTypes.POINTY_TAIGA) {
            this.gen = new WorldGenMegaPineTree(false, false);
        } else if (type == BiomeTreeTypes.JUNGLE) {
            this.gen = new WorldGenMegaJungle(false, 10, 20, BlockPlanks.EnumType.JUNGLE.getMetadata(), BlockPlanks.EnumType.JUNGLE.getMetadata());
        }
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {

        BlockPos blockpos;

        for (int j = 0; j < this.count; ++j) {
            int k = randomGenerator.nextInt(16) + 8;
            int l = randomGenerator.nextInt(16) + 8;
            this.gen.func_175904_e();
            blockpos = currentWorld.getHeight(pos.add(k, 0, l));

            if (this.gen.generate(currentWorld, randomGenerator, blockpos)) {
                this.gen.func_180711_a(currentWorld, randomGenerator, blockpos);
            }
        }
    }

    @Override
    public int getTreesPerChunk() {
        return this.count;
    }

    @Override
    public void setTreesPerChunk(int count) {
        checkArgument(count >= 0, "Number of trees per chunk in BigTree Populator cannot be negative");
        this.count = count;
    }

    @Override
    public BiomeTreeType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setType(BiomeTreeType type) {
        // TODO Auto-generated method stub

    }

}
