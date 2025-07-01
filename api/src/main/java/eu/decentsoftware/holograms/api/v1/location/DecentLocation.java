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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An immutable location without yaw and pitch.
 * This class does not store the World object, but only the world name,
 * which makes it possible to use this class even if the world is not loaded.
 *
 * @author d0by
 * @since 2.10.0
 */
public final class DecentLocation {

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Create a new instance of DecentLocation.
     *
     * @param worldName The world name.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param z         The z coordinate.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public DecentLocation(@NotNull String worldName, double x, double y, double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the world name of this location.
     *
     * @return The world name of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    @NotNull
    public String getWorldName() {
        return this.worldName;
    }

    /**
     * Get the x coordinate of this location.
     *
     * @return The x coordinate of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public double getX() {
        return this.x;
    }

    /**
     * Get the block x coordinate of this location.
     *
     * @return The block x coordinate of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public int getBlockX() {
        return (int) Math.floor(this.x);
    }

    /**
     * Get the y coordinate of this location.
     *
     * @return The y coordinate of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public double getY() {
        return this.y;
    }

    /**
     * Get the block y coordinate of this location.
     *
     * @return The block y coordinate of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public int getBlockY() {
        return (int) Math.floor(this.y);
    }

    /**
     * Get the z coordinate of this location.
     *
     * @return The z coordinate of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public double getZ() {
        return this.z;
    }

    /**
     * Get the block z coordinate of this location.
     *
     * @return The block z coordinate of this location.
     * @since 2.10.0
     */
    @Contract(pure = true)
    public int getBlockZ() {
        return (int) Math.floor(this.z);
    }

    /**
     * Get the distance between this location and another location.
     *
     * @param location The other location.
     * @return The distance between the two locations.
     * @since 2.10.0
     */
    public double distance(@NotNull DecentLocation location) {
        return Math.sqrt(distanceSquared(location));
    }

    /**
     * Get the squared distance between this location and another location.
     *
     * @param location The other location.
     * @return The squared distance between the two locations.
     * @throws IllegalArgumentException If the locations are in different worlds.
     * @since 2.10.0
     */
    public double distanceSquared(@NotNull DecentLocation location) {
        if (!isSameWorld(location)) {
            throw new IllegalArgumentException("Cannot calculate distance between locations in different worlds");
        }
        double dx = getX() - location.getX();
        double dy = getY() - location.getY();
        double dz = getZ() - location.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Check if this location is in the same world as another location.
     *
     * @param location The other location.
     * @return True if the locations are in the same world.
     * @since 2.10.0
     */
    public boolean isSameWorld(@NotNull DecentLocation location) {
        return getWorldName().equals(location.getWorldName());
    }

    /**
     * Creates a new instance of {@link DecentLocation} with the same world name
     * and coordinates as this instance.
     *
     * @return A new {@link DecentLocation} instance that is a copy of this location.
     * @since 2.10.0
     */
    @Contract(value = " -> new", pure = true)
    @NotNull
    public DecentLocation copy() {
        return new DecentLocation(worldName, x, y, z);
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DecentLocation)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        DecentLocation other = (DecentLocation) obj;
        return worldName.equals(other.worldName)
                && x == other.x
                && y == other.y
                && z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldName, x, y, z);
    }

    @Contract(pure = true)
    @NotNull
    @Override
    public String toString() {
        return "DecentLocation{" +
                "world=" + worldName + "," +
                " x=" + x + "," +
                " y=" + y + "," +
                " z=" + z +
                "}";
    }

}
