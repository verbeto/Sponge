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

import com.google.common.base.Optional;
import org.spongepowered.api.block.BlockState;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Forest;

import java.util.Random;


public class TreePopulator extends SpongePopulator implements Forest {
    
    private int count;
    
    public TreePopulator(int count) {
        this.count = count;
    }

    @Override
    public void populate(World currentWorld, Chunk chunk, Random randomGenerator, BlockPos pos) {
        BiomeGenBase biomegenbase = currentWorld.getBiomeGenForCoords(pos.add(16, 0, 16));
        
        int finalCount = this.count;
        if(randomGenerator.nextInt(10) == 0) {
            finalCount++;
        }
        
        BlockPos blockpos;
        
        boolean doGen = TerrainGen.decorate(currentWorld, randomGenerator, pos, TREE);
        for (int j = 0; doGen && j < finalCount; ++j)
        {
            int k = randomGenerator.nextInt(16) + 8;
            int l = randomGenerator.nextInt(16) + 8;
            WorldGenAbstractTree worldgenabstracttree = biomegenbase.genBigTreeChance(randomGenerator);
            worldgenabstracttree.func_175904_e();
            blockpos = currentWorld.getHeight(pos.add(k, 0, l));

            if (worldgenabstracttree.generate(currentWorld, randomGenerator, blockpos))
            {
                worldgenabstracttree.func_180711_a(currentWorld, randomGenerator, blockpos);
            }
        }
    }

    @Override
    public int getTreesPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setTreesPerChunk(int count) {
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
    public Optional<BlockState> getTrunkMaterial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTrunkMaterial(BlockState material) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Optional<BlockState> getLeavesMaterial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLeavesMaterial(BlockState material) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Optional<BiomeTreeType> getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setType(BiomeTreeType type) {
        // TODO Auto-generated method stub
        
    }

}
