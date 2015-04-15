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
package org.spongepowered.mod.data.manipulators.tiles;

import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.mod.data.manipulators.tiles.TileManipulatorUtility.fillBeaconData;

import com.google.common.base.Optional;
import org.spongepowered.api.data.AbstractDataManipulator;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataPriority;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.manipulators.tileentities.BeaconData;
import org.spongepowered.api.potion.PotionEffectType;

public class SpongeBeaconData extends AbstractDataManipulator<BeaconData> implements BeaconData {

    private PotionEffectType primary;
    private PotionEffectType secondary;

    @Override
    public Optional<PotionEffectType> getPrimaryEffect() {
        return Optional.fromNullable(this.primary);
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        this.primary = effect;
    }

    @Override
    public Optional<PotionEffectType> getSecondaryEffect() {
        return Optional.fromNullable(this.secondary);
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        this.secondary = effect;
    }

    @Override
    public void clearEffects() {
        this.primary = null;
        this.secondary = null;
    }

    @Override
    public Optional<BeaconData> fill(DataHolder dataHolder) {
        return fillBeaconData(this, dataHolder) ? Optional.<BeaconData>of(this) : Optional.<BeaconData>absent();
    }

    @Override
    public Optional<BeaconData> fill(DataHolder dataHolder, DataPriority overlap) {
        return fill(dataHolder); // TODO handle the overlap priority
    }

    @Override
    public Optional<BeaconData> from(DataContainer container) {
        return null; // Basically just read from container about Primary or Secondary
    }

    @Override
    public int compareTo(BeaconData o) {
        return 0; // TODO properly implement
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = new MemoryDataContainer();
        container.set(of("Primary"), this.primary == null ? "NONE" : this.primary.getId());
        container.set(of("Secondary"), this.secondary == null ? "NONE" : this.secondary.getId());
        return container;
    }
}
