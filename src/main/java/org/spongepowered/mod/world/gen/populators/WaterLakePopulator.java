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

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.mod.interfaces.IFlaggedPopulator;

import java.util.List;
import java.util.Random;

public class WaterLakePopulator implements IFlaggedPopulator {

    private ChunkProviderSettings settings;

    public WaterLakePopulator(ChunkProviderSettings settings) {
        this.settings = settings;
    }

    @Override
    public void populate(IChunkProvider provider, Chunk chunk, Random rand, List<String> flags) {
        World worldObj = (World) chunk.getWorld();
        BlockPos blockpos = new BlockPos(chunk.getBlockMin().getX(), 0, chunk.getBlockMin().getZ());
        int x = chunk.getPosition().getX();
        int z = chunk.getPosition().getZ();
        boolean flag = flags.contains("VILLAGE");
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && !flag
                && rand.nextInt(this.settings.waterLakeChance) == 0) {
            if (TerrainGen.populate(provider, worldObj, rand, x, z, flag, LAKE)) {
                int k1 = rand.nextInt(16) + 8;
                int l1 = rand.nextInt(256);
                int i2 = rand.nextInt(16) + 8;
                (new WorldGenLakes(Blocks.water)).generate(worldObj, rand, blockpos.add(k1, l1, i2));
            }
        }
    }

    /**
     * If called by a system which does not recognize {@link IFlaggedPopulator}s
     * then we have to skip calling the forge event as we do not have the chunk
     * provider.
     */
    @Override
    public void populate(Chunk chunk, Random rand) {
        World worldObj = (World) chunk.getWorld();
        BlockPos blockpos = new BlockPos(chunk.getBlockMin().getX(), 0, chunk.getBlockMin().getZ());
        int x = chunk.getPosition().getX();
        int z = chunk.getPosition().getZ();
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && rand.nextInt(this.settings.waterLakeChance) == 0) {
            int k1 = rand.nextInt(16) + 8;
            int l1 = rand.nextInt(256);
            int i2 = rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(worldObj, rand, blockpos.add(k1, l1, i2));
        }
    }
}
