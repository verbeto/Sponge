/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
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
package org.spongepowered.mod.mixin.core.world.biome;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.GeneratorPopulator;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.mod.world.gen.DeferredGroundCoverLayer;

import com.google.common.collect.Lists;

@NonnullByDefault
@Mixin(BiomeGenBase.class)
public abstract class MixinBiomeGenBase implements BiomeType {

    @Shadow public String biomeName;

    @Shadow public float temperature;

    @Shadow public float rainfall;

    @Shadow public float minHeight;

    @Shadow public float maxHeight;

    @Shadow public IBlockState topBlock;

    @Shadow public IBlockState fillerBlock;

    @Shadow
    public abstract float getFloatTemperature(BlockPos pos);

    List<Populator> populators;
    List<GeneratorPopulator> genpopulators;
    List<GroundCoverLayer> groundcover;

    @Override
    public String getName() {
        return this.biomeName;
    }

    @Override
    public double getTemperature() {
        return this.temperature;
    }

    @Override
    public double getHumidity() {
        return this.rainfall;
    }

    @Override
    public float getMinHeight() {
        return this.minHeight;
    }

    @Override
    public float getMaxHeight() {
        return this.maxHeight;
    }

    @Override
    public List<Populator> getPopulators() {
        return this.populators;
    }

    @Override
    public List<GroundCoverLayer> getGroundCover() {
        return this.groundcover;
    }

    @Override
    public List<GeneratorPopulator> getGeneratorPopulators() {
        return this.genpopulators;
    }

    /*
     * Initialize the various lists with values determined from the standard
     * settings for the biome and biome decorator. We later replace the standard
     * decoration with calls to these seeded lists.
     */
    @Inject(method = "<init>(IZ)V", at = @At("RETURN"))
    public void onConstructed(int id, boolean register, CallbackInfo ci) {
        populators = Lists.newArrayList();
        genpopulators = Lists.newArrayList();
        groundcover = Lists.newArrayList();
        groundcover.add(new DeferredGroundCoverLayer((BiomeGenBase)(Object)this, this.topBlock, 0, 1));
        groundcover.add(new DeferredGroundCoverLayer((BiomeGenBase)(Object)this, this.fillerBlock, 1));
    }

    /*
     * Calculate the depth of a layer based on the base and variance. In order
     * to replicate vanilla functionality however we first check for a
     * DeferredGroundCoverLayer and in this case we calculate the depth from the
     * stoneNoise.
     */
    private int getDepth(GroundCoverLayer layer, Random rand, double stoneNoise) {
        if (layer instanceof DeferredGroundCoverLayer) {
            return ((DeferredGroundCoverLayer) layer).getDepth(rand, stoneNoise);
        }
        return (int) (layer.getBaseDepth() + rand.nextDouble() * layer.getDepthVariance());
    }

    /*
     * We overwrite the placement of the biome top and filler blocks in order to
     * defer to our ground cover list, which we earlier seeded with the default
     * top and filler blocks for regularly registered biomes.
     */
    @Overwrite
    public void generateBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunk, int x, int z, double stoneNoise) {
        IBlockState currentPlacement = this.topBlock;
        int k = -1;
        int relativeX = x & 15;
        int relativeZ = z & 15;
        int i = 0;
        for (int currentY = 255; currentY >= 0; --currentY) {
            if (currentY <= rand.nextInt(5)) {
                chunk.setBlockState(relativeZ, currentY, relativeX, Blocks.bedrock.getDefaultState());
            } else {
                IBlockState nextBlock = chunk.getBlockState(relativeZ, currentY, relativeX);

                if (nextBlock.getBlock().getMaterial() == Material.air) {
                    k = -1;
                } else if (nextBlock.getBlock() == Blocks.stone) {
                    if (k == -1) {
                        if (this.groundcover.isEmpty()) {
                            k = 0;
                            continue;
                        }
                        i = 0;
                        GroundCoverLayer layer = this.groundcover.get(i);
                        currentPlacement = (IBlockState) layer.getState();
                        k = getDepth(layer, rand, stoneNoise);
                        if (k <= 0) {
                            continue;
                        }

                        if (currentY >= 62) {
                            chunk.setBlockState(relativeZ, currentY, relativeX, currentPlacement);
                            ++i;
                            if (i < this.groundcover.size()) {
                                layer = this.groundcover.get(i);
                                k = getDepth(layer, rand, stoneNoise);
                                currentPlacement = (IBlockState) layer.getState();
                            }
                        } else if (currentY < 56 - k) {
                            k = 0;
                            chunk.setBlockState(relativeZ, currentY, relativeX, Blocks.gravel.getDefaultState());
                        } else {
                            ++i;
                            if (i < this.groundcover.size()) {
                                layer = this.groundcover.get(i);
                                k = getDepth(layer, rand, stoneNoise);
                                currentPlacement = (IBlockState) layer.getState();
                                chunk.setBlockState(relativeZ, currentY, relativeX, currentPlacement);
                            }
                        }
                    } else if (k > 0) {
                        --k;
                        chunk.setBlockState(relativeZ, currentY, relativeX, currentPlacement);

                        if (k == 0) {
                            ++i;
                            if (i < this.groundcover.size()) {
                                GroundCoverLayer layer = this.groundcover.get(i);
                                k = getDepth(layer, rand, stoneNoise);
                                currentPlacement = (IBlockState) layer.getState();
                            }
                        }
                    }
                }
            }
        }
    }

}
