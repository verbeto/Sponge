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

import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.data.types.DoubleSizePlantType;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.DoublePlant;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DoublePlantPopulator extends SpongePopulator implements DoublePlant {

    private int baseChance;
    private int chanceVariance;
    private int attemptsPerCount;
    private WorldGenDoublePlant gen;
    private List<BlockDoublePlant.EnumPlantType> possibleTypes;

    public DoublePlantPopulator(int baseChance, int chanceVariance, int attemptsPerCount, BlockDoublePlant.EnumPlantType... types) {
        this.baseChance = baseChance;
        this.chanceVariance = chanceVariance;
        this.gen = new WorldGenDoublePlant();
        this.possibleTypes = Lists.newArrayList(types);
        this.attemptsPerCount = attemptsPerCount;
    }

    @Override
    protected void populate(World world, Chunk chunk, Random random, BlockPos pos) {
        int i, j, k, l;
        i = random.nextInt(this.chanceVariance) + this.baseChance;

        j = 0;

        while (j < i) {
            k = random.nextInt(this.possibleTypes.size());
            BlockDoublePlant.EnumPlantType type = this.possibleTypes.get(k);
            this.gen.func_180710_a(type);

            l = 0;

            while (true) {
                if (l < this.attemptsPerCount) {
                    int j1 = random.nextInt(16) + 8;
                    int k1 = random.nextInt(16) + 8;
                    // changed to use safeNextInt here
                    int i1 = safeNextInt(random, world.getHeight(pos.add(j1, 0, k1)).getY() + 32);

                    if (!this.gen.generate(world, random, new BlockPos(pos.getX() + j1, i1, pos.getZ() + k1))) {
                        ++l;
                        continue;
                    }
                }

                ++j;
                break;
            }
        }
    }

    @Override
    public int getBaseAmount() {
        return this.baseChance;
    }

    @Override
    public void setBaseAmount(int baseChance) {
        this.baseChance = baseChance;
    }

    @Override
    public int getAmountVariance() {
        return this.chanceVariance;
    }

    @Override
    public void setAmountVariance(int variance) {
        this.chanceVariance = variance;
    }

    @Override
    public ImmutableSet<DoubleSizePlantType> getPossibleTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPossibleTypes(Collection<DoubleSizePlantType> types)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPossibleTypes(DoubleSizePlantType... types)
    {
        // TODO Auto-generated method stub
        
    }

}
