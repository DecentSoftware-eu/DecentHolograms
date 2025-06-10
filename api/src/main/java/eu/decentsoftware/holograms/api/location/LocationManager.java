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

package eu.decentsoftware.holograms.api.location;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a position manager for holograms. This manager is used
 * for storing absolute or relative location of the hologram.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface LocationManager {

    /**
     * Gets the base location of this hologram, without any offsets or animations applied.
     *
     * @return The base location of this hologram.
     * @since 2.10.0
     */
    @NotNull
    DecentLocation getLocation();

    /**
     * Sets the base location of this hologram, without any offsets or animations applied.
     *
     * @param location The new base location to set for this hologram.
     * @since 2.10.0
     */
    void setLocation(@NotNull DecentLocation location);

    /**
     * Gets the final location of this hologram with all offsets and animations applied.
     * This includes any bound location and vector offsets.
     *
     * @return The final calculated location of this hologram.
     * @since 2.10.0
     */
    @NotNull
    DecentLocation getActualLocation();

    /**
     * Binds this hologram to a position binder that will dynamically provide its position.
     * This allows the hologram to move together with entities or follow predefined paths.
     *
     * @param locationBinder The supplier providing dynamic locations or null to unbind.
     * @since 2.10.0
     */
    void bindLocation(@Nullable LocationBinder locationBinder);

    /**
     * Removes any existing position binder from this hologram.
     *
     * @see #bindLocation(LocationBinder)
     * @since 2.10.0
     */
    default void unbindLocation() {
        bindLocation(null);
    }

    /**
     * Gets the current position binder if this hologram is bound.
     *
     * @return The currently active position binder, or null if not bound
     * @see #bindLocation(LocationBinder)
     * @since 2.10.0
     */
    @Nullable
    LocationBinder getLocationBinder();

    /**
     * Checks whether this hologram is currently bound to a position binder.
     *
     * @return True if bound to a position binder, false otherwise
     * @see #bindLocation(LocationBinder)
     * @since 2.10.0
     */
    default boolean isLocationBound() {
        return getLocationBinder() != null;
    }

    /**
     * Gets the current offsets applied to this hologram's position.
     * Offsets are used to adjust the final position relative to the base location.
     *
     * @return The current offsets applied to this hologram
     * @since 2.10.0
     */
    @NotNull
    DecentOffsets getOffsets();

    /**
     * Sets the offsets to apply to this hologram's position.
     * The offsets will be added to the base location when calculating the final position.
     *
     * @param offsets The new offsets to apply
     * @since 2.10.0
     */
    void setOffsets(@NotNull DecentOffsets offsets);

    /**
     * Resets the offsets to their default values, effectively removing any custom adjustments.
     * This will set the offsets to zero for all axes.
     *
     * @since 2.10.0
     */
    default void resetOffsets() {
        setOffsets(DecentOffsets.ZERO);
    }

}