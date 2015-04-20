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

import com.google.common.collect.ImmutableList;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.BIG_SHROOM;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.util.VariableAmount;
import org.spongepowered.api.util.WeightedRandomObject;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.BigMushroom;
import org.spongepowered.api.world.gen.types.MushroomType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Random;

@Mixin(WorldGenBigMushroom.class)
public abstract class MixinWorldGenMushroom implements BigMushroom {

    @Shadow private int mushroomType;
    
    @Shadow public abstract boolean generate(World worldIn, Random rand, BlockPos position);
    
    private VariableAmount bigMushroomsPerChunk;
    private WeightedRandomObject<MushroomType>[] types;
    
    @Inject(method = "<init>(Lnet/minecraft/block/Block;I)V", at = @At("RETURN"))
    public void onConstructed(int type, CallbackInfo ci) {
        if(type == 0) {
            
        }
    }

    @Override
    public void populate(Chunk chunk, Random random) {
        int x, z;
        int cx = chunk.getBlockMin().getX();
        int cz = chunk.getBlockMin().getZ();
        World world = (World) chunk.getWorld();
        BlockPos pos = new BlockPos(cx, 0, cz);
        
        boolean doGen = TerrainGen.decorate(world, random, pos, BIG_SHROOM);
        int totalWeight = 0;
        for (int j = 0; doGen && j < this.bigMushroomsPerChunk.getFlooredValue(random, cx, cz); ++j) {
            x = random.nextInt(16) + 8;
            z = random.nextInt(16) + 8;
            generate(world, random, world.getHeight(pos.add(x, 0, z)));
        }
    }

    @Override
    public Collection<WeightedRandomObject<MushroomType>> getPossibleTypes() {
        return ImmutableList.copyOf(this.types);
    }

    @Override
    public VariableAmount getMushroomsPerChunk() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMushroomsPerChunk(VariableAmount count) {
        // TODO Auto-generated method stub
        
    }

}
