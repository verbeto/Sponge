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
package org.spongepowered.mod.data.manipulators;

import com.google.common.base.Optional;
import org.spongepowered.api.data.AbstractDataManipulator;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataManipulator;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.manipulators.MobSpawnerData;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.WeightedRandomEntity;

import java.util.Collection;

public class SpongeMobSpawnerData extends AbstractDataManipulator<MobSpawnerData> implements MobSpawnerData {

    private short remainingDelay;
    private short minimumSpawnDelay;
    private short maximumSpawnDelay;
    private short spawnCount;
    private short maximumNearbyEntities;
    private short requiredPlayerRange;
    private short spawnRange;

    @Override
    public Optional<MobSpawnerData> fill(DataHolder dataHolder) {
        return null;
    }

    @Override
    public Optional<MobSpawnerData> fill(DataHolder dataHolder,
            DataPriority overlap) {
        return null;
    }

    @Override
    public Optional<MobSpawnerData> from(DataContainer container) {
        return null;
    }

    @Override
    public int compareTo(MobSpawnerData o) {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short getRemainingDelay() {
        return this.remainingDelay;
    }

    @Override
    public void setRemainingDelay(short delay) {
        this.remainingDelay = delay;
    }

    @Override
    public short getMinimumSpawnDelay() {
        return this.minimumSpawnDelay;
    }

    @Override
    public void setMinimumSpawnDelay(short delay) {
        this.minimumSpawnDelay = delay;
    }

    @Override
    public short getMaximumSpawnDelay() {
        return this.maximumSpawnDelay;
    }

    @Override
    public void setMaximumSpawnDelay(short delay) {
        this.maximumSpawnDelay = delay;
    }

    @Override
    public short getSpawnCount() {
        return this.spawnCount;
    }

    @Override
    public void setSpawnCount(short count) {
        this.spawnCount = count;
    }

    @Override
    public short getMaximumNearbyEntities() {
        return this.maximumNearbyEntities;
    }

    @Override
    public void setMaximumNearbyEntities(short count) {
        this.maximumNearbyEntities = count;
    }

    @Override
    public short getRequiredPlayerRange() {
        return this.requiredPlayerRange;
    }

    @Override
    public void setRequiredPlayerRange(short range) {
        this.requiredPlayerRange = range;
    }

    @Override
    public short getSpawnRange() {
        return this.spawnRange;
    }

    @Override
    public void setSpawnRange(short range) {
        this.spawnRange = range;
    }

    @Override
    public void setNextEntityToSpawn(EntityType type,
            DataContainer additionalProperties) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNextEntityToSpawn(WeightedRandomEntity entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<WeightedRandomEntity> getPossibleEntitiesToSpawn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPossibleEntitiesToSpawn(WeightedRandomEntity... entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPossibleEntitiesToSpawn(
            Collection<WeightedRandomEntity> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<WeightedRandomEntity> addWeightedEntity(
            EntityType entityType, int weight,
            Collection<DataManipulator<?>> additionalProperties) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeWeightedEntity(WeightedRandomEntity weightedEntity) {
        // TODO Auto-generated method stub

    }
}
