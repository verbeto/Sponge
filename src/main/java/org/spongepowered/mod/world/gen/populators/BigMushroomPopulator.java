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
import org.spongepowered.api.data.types.BigMushroomType;

import static com.google.common.base.Preconditions.checkArgument;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.BigMushroom;

import java.util.Random;

public class BigMushroomPopulator extends SpongePopulator implements BigMushroom {

    private int bigMushroomsPerChunk;
    private WorldGenerator gen;
    //private BigMushroomType type;

    public BigMushroomPopulator(int bigMushroomsPerChunk) {
        this.bigMushroomsPerChunk = bigMushroomsPerChunk;
        this.gen = new WorldGenBigMushroom();
        // this.type = BigMushroomTypes.RANDOM;
    }

    /*public BigMushroomPopulator(int bigMushroomsPerChunk, BigMushroomType type) {
        this.bigMushroomsPerChunk = bigMushroomsPerChunk;
        this.type = type;
        if (type == BigMushroomTypes.RANDOM) {
            this.gen = new WorldGenBigMushroom();
        } else if (type == BigMushroomTypes.BROWN) {
            this.gen = new WorldGenBigMushroom(0);
        } else if (type == BigMushroomTypes.RED) {
            this.gen = new WorldGenBigMushroom(1);
        }
    }*/

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {

        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, BIG_SHROOM);
        for (int j = 0; doGen && j < this.bigMushroomsPerChunk; ++j) {
            int k = randomGenerator.nextInt(16) + 8;
            int l = randomGenerator.nextInt(16) + 8;
            this.gen.generate(currentWorld, randomGenerator, currentWorld.getHeight(pos.add(k, 0, l)));
        }
    }

    /*@Override
    public BigMushroomType getType() {
        return this.type;
    }

    @Override
    public void setType(BigMushroomType type) {
        checkNotNull(type, "type");
        if (type != this.type) {
            this.type = type;
            if (type == BigMushroomTypes.RANDOM) {
                this.gen = new WorldGenBigMushroom();
            } else if (type == BigMushroomTypes.BROWN) {
                this.gen = new WorldGenBigMushroom(0);
            } else if (type == BigMushroomTypes.RED) {
                this.gen = new WorldGenBigMushroom(1);
            }
        }
    }*/

    @Override
    public int getMushroomsPerChunk() {
        return this.bigMushroomsPerChunk;
    }

    @Override
    public void setMushroomsPerChunk(int count) {
        checkArgument(count >= 0, "Number of mushrooms per chunk in BigMushroom Populator cannot be negative");
        this.bigMushroomsPerChunk = count;
    }

    @Override
    public boolean usesRandomizedType() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void useRandomizedTypes(boolean state) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Optional<BigMushroomType> getType()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setType(BigMushroomType type)
    {
        // TODO Auto-generated method stub
        
    }

}
