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

import com.google.common.base.Optional;
import org.spongepowered.api.data.types.PlantType;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Flowers;

import java.util.Random;

public class FlowerPopulator extends SpongePopulator implements Flowers {

    private static NoiseGeneratorPerlin NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);

    private int count;
    private WorldGenFlowers gen;

    public FlowerPopulator(int count) {
        this.count = count;
        this.gen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {
        BiomeGenBase biomegenbase = currentWorld.getBiomeGenForCoords(pos.add(16, 0, 16));
        double d0 = NOISE.func_151601_a((double) (pos.getX() + 8) / 200.0D, (double) (pos.getZ() + 8) / 200.0D);
        double mod = 1;
        if (biomegenbase instanceof BiomeGenPlains) {
            if (d0 < -0.8D) {
                // This is to match vanilla, might be a little weird for API
                // users though, should consider how to expose this
                mod = 3.75;
            }
        }

        BlockPos blockpos;

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, FLOWERS);
        for (int j = 0; doGen && j < this.count * mod; ++j) {
            int k = randomGenerator.nextInt(16) + 8;
            int l = randomGenerator.nextInt(16) + 8;
            int i1 = safeNextInt(randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)).getY() + 32);
            blockpos = pos.add(k, i1, l);
            BlockFlower.EnumFlowerType enumflowertype = biomegenbase.pickRandomFlower(randomGenerator, blockpos);
            BlockFlower blockflower = enumflowertype.getBlockType().getBlock();

            if (blockflower.getMaterial() != Material.air) {
                this.gen.setGeneratedBlock(blockflower, enumflowertype);
                this.gen.generate(currentWorld, randomGenerator, blockpos);
            }
        }
    }

    @Override
    public int getFlowersPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setFlowersPerChunk() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isBiomeDependent() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setBiomeDependent(boolean state) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<PlantType> getFlowerType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void serFlowerType(PlantType type) {
        // TODO Auto-generated method stub

    }

}
