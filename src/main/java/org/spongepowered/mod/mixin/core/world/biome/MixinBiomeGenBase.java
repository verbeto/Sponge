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

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.VariableAmount;
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
import org.spongepowered.mod.world.gen.populators.CactusPopulator;
import org.spongepowered.mod.world.gen.populators.ClayPopulator;
import org.spongepowered.mod.world.gen.populators.DeadBushPopulator;
import org.spongepowered.mod.world.gen.populators.FlowerPopulator;
import org.spongepowered.mod.world.gen.populators.LiquidsPopulator;
import org.spongepowered.mod.world.gen.populators.OrePopulator;
import org.spongepowered.mod.world.gen.populators.PumpkinPopulator;
import org.spongepowered.mod.world.gen.populators.ReedPopulator;
import org.spongepowered.mod.world.gen.populators.SandPopulator;
import org.spongepowered.mod.world.gen.populators.SmallMushroomPopulator;
import org.spongepowered.mod.world.gen.populators.TallGrassPopulator;
import org.spongepowered.mod.world.gen.populators.TreePopulator;
import org.spongepowered.mod.world.gen.populators.WaterLilyPopulator;

import java.util.List;
import java.util.Random;

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

    @Shadow public BiomeDecorator theBiomeDecorator;

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
    private void onConstructed(int id, boolean register, CallbackInfo ci) {
        this.populators = Lists.newArrayList();
        this.genpopulators = Lists.newArrayList();
        this.groundcover = Lists.newArrayList();
        this.buildPopulators();
    }

    protected void buildPopulators() {
        this.groundcover.add(new GroundCoverLayer((BlockState) this.topBlock, VariableAmount.fixed(1)));
        this.groundcover.add(new GroundCoverLayer((BlockState) this.fillerBlock, VariableAmount.fixed(3)));

        this.populators.add(new OrePopulator.DirtPopulator());
        this.populators.add(new OrePopulator.GravelPopulator());
        this.populators.add(new OrePopulator.DioritePopulator());
        this.populators.add(new OrePopulator.GranitePopulator());
        this.populators.add(new OrePopulator.AndesitePopulator());
        this.populators.add(new OrePopulator.CoalPopulator());
        this.populators.add(new OrePopulator.IronPopulator());
        this.populators.add(new OrePopulator.GoldPopulator());
        this.populators.add(new OrePopulator.RedstonePopulator());
        this.populators.add(new OrePopulator.DiamondPopulator());
        this.populators.add(new OrePopulator.LapisPopulator());

        if (this.theBiomeDecorator.sandPerChunk2 > 0) {
            this.populators.add(new SandPopulator(Blocks.sand, this.theBiomeDecorator.sandPerChunk2, 7));
        }
        if (this.theBiomeDecorator.clayPerChunk > 0) {
            this.populators.add(new ClayPopulator(this.theBiomeDecorator.clayPerChunk, 4));
        }
        if (this.theBiomeDecorator.sandPerChunk > 0) {
            this.populators.add(new SandPopulator(Blocks.gravel, this.theBiomeDecorator.sandPerChunk, 6));
        }
        if (this.theBiomeDecorator.treesPerChunk > 0) {
            this.populators.add(new TreePopulator(this.theBiomeDecorator.treesPerChunk));
        }
        if (this.theBiomeDecorator.bigMushroomsPerChunk > 0) {
            //this.populators.add(new BigMushroomPopulator(this.theBiomeDecorator.bigMushroomsPerChunk));
        }
        if (this.theBiomeDecorator.flowersPerChunk > 0) {
            this.populators.add(new FlowerPopulator(this.theBiomeDecorator.flowersPerChunk));
        }
        if (this.theBiomeDecorator.grassPerChunk > 0) {
            this.populators.add(new TallGrassPopulator(this.theBiomeDecorator.grassPerChunk));
        }
        if (this.theBiomeDecorator.deadBushPerChunk > 0) {
            this.populators.add(new DeadBushPopulator(this.theBiomeDecorator.deadBushPerChunk));
        }
        if (this.theBiomeDecorator.waterlilyPerChunk > 0) {
            this.populators.add(new WaterLilyPopulator(this.theBiomeDecorator.waterlilyPerChunk));
        }
        if (this.theBiomeDecorator.mushroomsPerChunk > 0) {
            this.populators.add(new SmallMushroomPopulator(this.theBiomeDecorator.mushroomsPerChunk));
        }
        if (this.theBiomeDecorator.reedsPerChunk > 0) {
            this.populators.add(new ReedPopulator(this.theBiomeDecorator.reedsPerChunk));
        }
        this.populators.add(new PumpkinPopulator());
        if (this.theBiomeDecorator.cactiPerChunk > 0) {
            this.populators.add(new CactusPopulator(this.theBiomeDecorator.cactiPerChunk));
        }

        if (this.theBiomeDecorator.generateLakes) {
            this.populators.add(new LiquidsPopulator());
        }
    }

    /**
     * @author Deamon
     * 
     *         We overwrite the placement of the biome top and filler blocks in
     *         order to defer to our ground cover list, which we earlier seeded
     *         with the default top and filler blocks for regularly registered
     *         biomes.
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
                        k = layer.getDepth().getFlooredValue(rand, x, z);
                        if (k <= 0) {
                            continue;
                        }

                        if (currentY >= 62) {
                            chunk.setBlockState(relativeZ, currentY, relativeX, currentPlacement);
                            ++i;
                            if (i < this.groundcover.size()) {
                                layer = this.groundcover.get(i);
                                k = layer.getDepth().getFlooredValue(rand, x, z);
                                currentPlacement = (IBlockState) layer.getState();
                            }
                        } else if (currentY < 56 - k) {
                            k = 0;
                            chunk.setBlockState(relativeZ, currentY, relativeX, Blocks.gravel.getDefaultState());
                        } else {
                            ++i;
                            if (i < this.groundcover.size()) {
                                layer = this.groundcover.get(i);
                                k = layer.getDepth().getFlooredValue(rand, x, z);
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
                                k = layer.getDepth().getFlooredValue(rand, x, z);
                                currentPlacement = (IBlockState) layer.getState();
                            }
                        }
                    }
                }
            }
        }
    }

}
