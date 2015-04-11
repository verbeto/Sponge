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

import org.spongepowered.api.block.BlockState;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.ANDESITE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.COAL;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIAMOND;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIORITE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.DIRT;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GOLD;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GRANITE;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.GRAVEL;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.IRON;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.LAPIS;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.REDSTONE;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populators.Ore;

import java.util.Random;

public abstract class OrePopulator extends SpongePopulator implements Ore {

    public static class DirtPopulator extends OrePopulator {

        public DirtPopulator() {
            super(Blocks.dirt.getDefaultState(), DIRT);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.dirtSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.dirtCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.dirtMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.dirtMaxHeight;
        }

    }
    public static class GravelPopulator extends OrePopulator {

        public GravelPopulator() {
            super(Blocks.gravel.getDefaultState(), GRAVEL);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.gravelSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.gravelCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.gravelMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.gravelMaxHeight;
        }

    }
    public static class DioritePopulator extends OrePopulator {

        public DioritePopulator() {
            super(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), DIORITE);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.dioriteSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.dioriteCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.dioriteMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.dioriteMaxHeight;
        }

    }
    public static class GranitePopulator extends OrePopulator {

        public GranitePopulator() {
            super(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), GRANITE);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.graniteSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.graniteCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.graniteMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.graniteMaxHeight;
        }

    }
    public static class AndesitePopulator extends OrePopulator {

        public AndesitePopulator() {
            super(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), ANDESITE);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.andesiteSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.andesiteCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.andesiteMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.andesiteMaxHeight;
        }

    }
    public static class CoalPopulator extends OrePopulator {

        public CoalPopulator() {
            super(Blocks.coal_ore.getDefaultState(), COAL);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.coalSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.coalCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.coalMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.coalMaxHeight;
        }

    }
    public static class IronPopulator extends OrePopulator {

        public IronPopulator() {
            super(Blocks.iron_ore.getDefaultState(), IRON);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.ironSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.ironCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.ironMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.ironMaxHeight;
        }

    }
    public static class GoldPopulator extends OrePopulator {

        public GoldPopulator() {
            super(Blocks.gold_ore.getDefaultState(), GOLD);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.goldSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.goldCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.goldMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.goldMaxHeight;
        }

    }
    public static class RedstonePopulator extends OrePopulator {

        public RedstonePopulator() {
            super(Blocks.redstone_ore.getDefaultState(), REDSTONE);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.redstoneSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.redstoneCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.redstoneMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.redstoneMaxHeight;
        }

    }
    public static class DiamondPopulator extends OrePopulator {

        public DiamondPopulator() {
            super(Blocks.diamond_ore.getDefaultState(), DIAMOND);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.diamondSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.diamondCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.diamondMinHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.diamondMaxHeight;
        }

    }
    public static class LapisPopulator extends OrePopulator {

        public LapisPopulator() {
            super(Blocks.lapis_ore.getDefaultState(), LAPIS);
        }

        @Override
        protected int getSize(ChunkProviderSettings settings) {
            return settings.lapisSize;
        }

        @Override
        protected int getCount(ChunkProviderSettings settings) {
            return settings.lapisCount;
        }

        @Override
        protected int getMin(ChunkProviderSettings settings) {
            return settings.lapisCenterHeight;
        }

        @Override
        protected int getMax(ChunkProviderSettings settings) {
            return settings.lapisSpread;
        }

        @Override
        protected void genOre(int count, WorldGenerator gen, int height, int spread, BlockPos pos, Random rand, World world) {
            for (int l = 0; l < count; ++l) {
                BlockPos blockpos = pos.add(rand.nextInt(16), rand.nextInt(spread) + rand.nextInt(spread) + height - spread, rand.nextInt(16));
                gen.generate(world, rand, blockpos);
            }
        }

    }

    private EventType type;
    private IBlockState ore;
    private int cachedSize = 0;
    private WorldGenerator gen = null;

    protected OrePopulator(IBlockState ore, EventType type) {
        this.type = type;
        this.ore = ore;
    }

    protected abstract int getSize(ChunkProviderSettings settings);
    protected abstract int getCount(ChunkProviderSettings settings);
    protected abstract int getMin(ChunkProviderSettings settings);
    protected abstract int getMax(ChunkProviderSettings settings);

    @Override
    protected void populate(World currentWorld, Chunk chunk, Random random, BlockPos pos) {
        ChunkProviderSettings settings;
        String s = currentWorld.getWorldInfo().getGeneratorOptions();
        if (s != null) {
            settings = ChunkProviderSettings.Factory.func_177865_a(s).func_177864_b();
        } else {
            settings = ChunkProviderSettings.Factory.func_177865_a("").func_177864_b();
        }
        int size = getSize(settings);
        int count = getCount(settings);
        int min = getMin(settings);
        int max = getMax(settings);

        // refresh the generator if the size has changed from the cached size
        if (size != this.cachedSize || this.gen == null) {
            this.gen = new WorldGenMinable(this.ore, size);
        }

        if (TerrainGen.generateOre(currentWorld, random, this.gen, pos, this.type)) {
            genOre(count, this.gen, min, max, pos, random, currentWorld);
        }
    }

    protected void genOre(int count, WorldGenerator gen, int min, int max, BlockPos pos, Random rand, World world) {
        int l;

        if (max < min) {
            l = min;
            min = max;
            max = l;
        } else if (max == min) {
            if (min < 255) {
                ++max;
            } else {
                --min;
            }
        }

        for (l = 0; l < count; ++l) {
            BlockPos blockpos = pos.add(rand.nextInt(16), rand.nextInt(max - min) + min, rand.nextInt(16));
            gen.generate(world, rand, blockpos);
        }
    }

    @Override
    public BlockState getOreBlock() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOreBlock(BlockState block) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getDepositSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setDepositSize(int size) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getDepositsPerChunk() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setDepositsPerChunk(int count) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getMinHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMinHeight(int min) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getMaxHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setMaxHeight(int max) {
        // TODO Auto-generated method stub
        
    }

}
