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
package org.spongepowered.mod.mixin.core.world.gen;

import org.spongepowered.api.world.Chunk;
import net.minecraft.block.BlockFalling;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(ChunkProviderEnd.class)
public abstract class MixinChunkProviderEnd implements IChunkProvider {

    @Shadow private int chunkX;
    @Shadow private int chunkZ;
    @Shadow private World endWorld;
    @Shadow private Random endRNG;

    /**
     * @author Deamon
     * 
     *         We overwrite this function with just the forge event call as the
     *         functionality of replacing the stone blocks with end stone has
     *         been moved to a genpop (EndBiomeGeneratorPopulator) attached to
     *         BiomeGenEnd.
     */
    @Overwrite
    public void func_180519_a(ChunkPrimer primer) {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, chunkX, chunkZ, primer, this.endWorld);
        MinecraftForge.EVENT_BUS.post(event);

        // BEGIN removed code

//        if (event.getResult() == Result.DENY) return;
//        for (int i = 0; i < 16; ++i)
//        {
//            for (int j = 0; j < 16; ++j)
//            {
//                byte b0 = 1;
//                int k = -1;
//                IBlockState iblockstate = Blocks.end_stone.getDefaultState();
//                IBlockState iblockstate1 = Blocks.end_stone.getDefaultState();
//
//                for (int l = 127; l >= 0; --l)
//                {
//                    IBlockState iblockstate2 = primer.getBlockState(i, l, j);
//
//                    if (iblockstate2.getBlock().getMaterial() == Material.air)
//                    {
//                        k = -1;
//                    }
//                    else if (iblockstate2.getBlock() == Blocks.stone)
//                    {
//                        if (k == -1)
//                        {
//                            if (b0 <= 0)
//                            {
//                                iblockstate = Blocks.air.getDefaultState();
//                                iblockstate1 = Blocks.end_stone.getDefaultState();
//                            }
//
//                            k = b0;
//
//                            if (l >= 0)
//                            {
//                                primer.setBlockState(i, l, j, iblockstate);
//                            }
//                            else
//                            {
//                                primer.setBlockState(i, l, j, iblockstate1);
//                            }
//                        }
//                        else if (k > 0)
//                        {
//                            --k;
//                            primer.setBlockState(i, l, j, iblockstate1);
//                        }
//                    }
//                }
//            }
//        }

        // END removed code
    }

    /**
     * @author Deamon
     * 
     * We overwrite this chunk in order to replace the call to the end biome
     * decorator with a call to our biome populators for the correct chunk.
     */
    @Overwrite
    public void populate(IChunkProvider provider, int x, int z) {
        BlockFalling.fallInstantly = true;

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(provider, endWorld, endWorld.rand, x, z, false));

        //BEGIN sponge code
        // Since we're not calling the biome decorate forge events here(to stay
        // consistent with standard behavior) we instead directly call the
        // biome's populators

        Chunk chunk = (Chunk) this.endWorld.getChunkFromChunkCoords(x, z);
        if (chunk == null) {
            // When the chunk is null, there's a bug somewhere in the server
            // Better not pass this null value to all plugins, that will make
            // it look like the plugins are in error
            throw new NullPointerException("Failed to populate chunk at (" + x + "," + z + ")");
        }
        for (Populator populator : ((org.spongepowered.api.world.World) this.endWorld).getBiome(x * 16 + 15, z * 16 + 15)
                .getPopulators()) {
            populator.populate(chunk, this.endRNG);
        }

        // END sponge code

        // BEGIN removed code

//        BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
//        this.endWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, blockpos);

        // BEGIN END code

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(provider, endWorld, endWorld.rand, x, z, false));

        BlockFalling.fallInstantly = false;
    }

}
