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

package eu.decentsoftware.holograms.api.v1.location;

import org.jetbrains.annotations.Contract;

/**
 * Represents a set of three-dimensional offsets, typically used to adjust the position
 * of objects in a 3D space. Each offset value corresponds to a specific axis (x, y, z).
 *
 * @author d0by
 * @since 2.10.0
 */
public final class DecentOffsets {

    public static final DecentOffsets ZERO = new DecentOffsets(0, 0, 0);
    private final double x;
    private final double y;
    private final double z;

    /**
     * Constructs a new Offsets object with the specified values for the x, y, and z axes.
     *
     * @param x The offset along the x-axis.
     * @param y The offset along the y-axis.
     * @param z The offset along the z-axis.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public DecentOffsets(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Retrieves the offset value along the x-axis in a 3D space.
     *
     * @return The x-axis offset value.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public double getX() {
        return x;
    }

    /**
     * Retrieves the offset value along the y-axis in a 3D space.
     *
     * @return The y-axis offset value.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public double getY() {
        return y;
    }

    /**
     * Retrieves the offset value along the z-axis in a 3D space.
     *
     * @return The z-axis offset value.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public double getZ() {
        return z;
    }

}
