/*
 * This file is part of DecentHolograms, licensed under the GNU GPL v3.0 License.
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.nms.api.display.data;

import com.google.common.base.Preconditions;

/**
 * Represents the brightness of a display, consisting of block light and sky light levels.
 * Both values are expected to be in the range of 0 to 15.
 *
 * @author d0by
 * @since 1.0.0
 */
public class DisplayBrightness {
    private final int blockLight;
    private final int skyLight;

    /**
     * Creates a new DisplayBrightness instance.
     *
     * @param blockLight the block light level, between 0-15
     * @param skyLight   the sky light level, between 0-15
     */
    private DisplayBrightness(int blockLight, int skyLight) {
        this.blockLight = blockLight;
        this.skyLight = skyLight;
    }

    /**
     * Gets the block lighting component of this brightness.
     *
     * @return block light, between 0-15
     */
    public int getBlockLight() {
        return this.blockLight;
    }

    /**
     * Gets the sky lighting component of this brightness.
     *
     * @return sky light, between 0-15
     */
    public int getSkyLight() {
        return this.skyLight;
    }

    /**
     * Creates a new DisplayBrightness instance with the specified block light and sky light levels.
     * Both blockLight and skyLight values must be between 0 and 15, inclusive.
     *
     * @param blockLight the block light level, must be within the range 0-15
     * @param skyLight   the sky light level, must be within the range 0-15
     * @return a DisplayBrightness instance with the specified blockLight and skyLight levels
     * @throws IllegalArgumentException if blockLight or skyLight is outside the valid range (0-15)
     */
    public static DisplayBrightness of(int blockLight, int skyLight) {
        Preconditions.checkArgument(0 <= blockLight && blockLight <= 15, "blockLight out of range 0-15: %s", blockLight);
        Preconditions.checkArgument(0 <= skyLight && skyLight <= 15, "skyLight out of range 0-15: %s", skyLight);
        return new DisplayBrightness(blockLight, skyLight);
    }
}
