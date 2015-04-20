/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <http://www.spongepowered.org>
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
package org.spongepowered.mod.mixin.core.world.populator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.BlockBlob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Random;

@Mixin(WorldGenBlockBlob.class)
public class MixinWorldGenBlockBlob implements BlockBlob {

    @Shadow private Block field_150545_a;
    @Shadow private int field_150544_b;

    private VariableAmount radius;
    private VariableAmount count;
    private IBlockState block;

    @Inject(method = "<init>(Lnet/minecraft/block/Block;I)V", at = @At("RETURN"))
    public void onConstructed(Block block, int radius, CallbackInfo ci) {
        this.radius = VariableAmount.baseAndVariance(radius+1, 1);
        this.block = block.getDefaultState();
        this.count = VariableAmount.fixed(4);
    }

    @Overwrite
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        //TODO Don't think this method decompiled very well, could use some cleanup since we're overwriting anyway
        while (true)
        {
            if (position.getY() > 3)
            {
                label47:
                {
                    if (!worldIn.isAirBlock(position.down()))
                    {
                        Block block = worldIn.getBlockState(position.down()).getBlock();

                        if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)
                        {
                            break label47;
                        }
                    }

                    position = position.down();
                    continue;
                }
            }

            if (position.getY() <= 3)
            {
                return false;
            }

//            int i1 = this.field_150544_b;

            for (int i = 0; i < 3; ++i)
            {
//                int j = i1 + rand.nextInt(2);
                int j = this.radius.getFlooredValue(rand, position.getX(), position.getZ());
//                int k = i1 + rand.nextInt(2);
                int k = this.radius.getFlooredValue(rand, position.getX(), position.getZ());
//                int l = i1 + rand.nextInt(2);
                int l = this.radius.getFlooredValue(rand, position.getX(), position.getZ());
                if(j < 0 || k < 0 || l < 0) {
                    continue;
                }
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                Iterator iterator = BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l)).iterator();

                while (iterator.hasNext())
                {
                    BlockPos blockpos1 = (BlockPos) iterator.next();

                    if (blockpos1.distanceSq(position) <= (double) (f * f))
                    {
//                        worldIn.setBlockState(blockpos1, this.field_150545_a.getDefaultState(), 4);
                        worldIn.setBlockState(blockpos1, this.block, 4);
                    }
                }

//                position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
                position = position.add(-j + rand.nextInt(j * 2), 0 - rand.nextInt(2), -l + rand.nextInt(l * 2));
            }

            return true;
        }
    }

    @Override
    public void populate(Chunk chunk, Random random) {
        int x, z;
        int cx = chunk.getBlockMin().getX();
        int cz = chunk.getBlockMin().getZ();
        World world = (World) chunk.getWorld();
        BlockPos pos = new BlockPos(cx, 0, cz);

        for (int i = 0; i < this.count.getFlooredValue(random, cx, cz); ++i) {
            x = random.nextInt(16) + 8;
            z = random.nextInt(16) + 8;
            BlockPos blockpos1 = world.getHeight(pos.add(x, 0, z));
            generate(world, random, blockpos1);
        }
    }

    @Override
    public BlockState getBlock() {
        return (BlockState) this.block;
    }

    @Override
    public void setBlock(BlockState state) {
        this.block = (IBlockState) state;
    }

    @Override
    public VariableAmount getRadius() {
        return this.radius;
    }

    @Override
    public void setRadius(VariableAmount radius) {
        this.radius = radius;
    }

    @Override
    public VariableAmount getCount() {
        return this.count;
    }

    @Override
    public void setCount(VariableAmount count) {
        this.count = count;
    }

}
