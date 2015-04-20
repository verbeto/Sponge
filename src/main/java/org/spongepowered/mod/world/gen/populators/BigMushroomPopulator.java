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

import com.google.common.collect.Sets;

import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.util.WeightedRandomObject;
import org.spongepowered.api.world.gen.types.MushroomType;
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
import java.util.Set;

public class BigMushroomPopulator extends SpongePopulator implements BigMushroom {

    private VariableAmount bigMushroomsPerChunk;
    private WeightedRandomObject<MushroomType>[] types;
    private WorldGenBigMushroom gen;

    public BigMushroomPopulator(VariableAmount bigMushroomsPerChunk) {
        this.bigMushroomsPerChunk = bigMushroomsPerChunk;
        this.gen = new WorldGenBigMushroom();
        this.types = Sets.newHashSet();
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {

    }

    @Override
    public Set<WeightedRandomObject<MushroomType>> getPossibleTypes() {
        return this.types;
    }

    @Override
    public VariableAmount getMushroomsPerChunk() {
        return this.bigMushroomsPerChunk;
    }

    @Override
    public void setMushroomsPerChunk(VariableAmount count) {
        this.bigMushroomsPerChunk = count;
    }

}
