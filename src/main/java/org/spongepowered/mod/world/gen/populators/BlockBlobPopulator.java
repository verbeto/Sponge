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
import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.BlockBlob;

import java.util.Iterator;
import java.util.Random;

public class BlockBlobPopulator extends SpongePopulator implements BlockBlob {

    private int count;
    private IBlockState block;
    private int radius;

    public BlockBlobPopulator(IBlockState block, int count, int radius) {
        this.count = count;
        this.block = block;
        this.radius = radius;
    }

    @Override
    protected void populate(World world, Chunk chunk, Random random, BlockPos pos) {
        int i, j, k, l;
        i = random.nextInt(this.count);

        for (j = 0; j < i; ++j) {
            k = random.nextInt(16) + 8;
            l = random.nextInt(16) + 8;
            BlockPos blockpos1 = world.getHeight(pos.add(k, 0, l));
            createBlob(world, random, blockpos1, this.radius, this.block);
        }
    }

    /**
     * This method is from
     * {@link net.minecraft.world.gen.feature.WorldGenBlockBlob#generate} but
     * modified to take a custom {@link IBlockState}.
     */
    private boolean createBlob(World worldIn, Random rand, BlockPos position, int radius, IBlockState state) {
        while (true) {
            if (position.getY() > 3) {
                label47 : {
                    if (!worldIn.isAirBlock(position.down())) {
                        Block block = worldIn.getBlockState(position.down()).getBlock();

                        if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone) {
                            break label47;
                        }
                    }

                    position = position.down();
                    continue;
                }
            }

            if (position.getY() <= 3) {
                return false;
            }

            int i1 = radius;

            for (int i = 0; i1 >= 0 && i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                Iterator iterator = BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l)).iterator();

                while (iterator.hasNext()) {
                    BlockPos blockpos1 = (BlockPos) iterator.next();

                    if (blockpos1.distanceSq(position) <= (double) (f * f)) {
                        // BEGIN sponge modifications
                        worldIn.setBlockState(blockpos1, state, 4);
                        // END sponge modifications
                    }
                }

                position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }

            return true;
        }
    }

    @Override
    public BlockState getBlock() {
        return (BlockState) this.block;
    }

    @Override
    public void setBlock(BlockState state) {
        checkNotNull(state, "state");
        this.block = (IBlockState) state;
    }

    @Override
    public int getBaseRadius() {
        return this.radius;
    }

    @Override
    public void setBaseRadius(int radius) {
        checkArgument(radius >= 0, "Radius of block blob cannot be negative");
        this.radius = radius;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public void setCount(int count) {
        checkArgument(count >= 0, "Number of blocks in block blob cannot be negative");
        this.count = count;
    }

    @Override
    public int getRadiusVariance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRadiusVariance(int variance) {
        // TODO Auto-generated method stub
        
    }

}
