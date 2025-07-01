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

package eu.decentsoftware.holograms.api.v1.hologram;

import eu.decentsoftware.holograms.api.v1.location.LocationManager;
import eu.decentsoftware.holograms.api.v1.visibility.VisibilityManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * This class represents a hologram. A hologram is a collection of components
 * that can be moved in the world. These components all together form a hologram
 * that's visible to specified players and has multiple pages.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface Hologram {

    /**
     * Get the page at the given index.
     *
     * @param index The index of the page to get.
     * @return The page at the given index.
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     * @since 2.10.0
     */
    @NotNull
    HologramPage getPage(int index);

    /**
     * Retrieves an unmodifiable list of all pages associated with this hologram.
     *
     * @return An unmodifiable list of {@link HologramPage} objects representing the pages of this hologram.
     * @since 2.10.0
     */
    @NotNull
    @Unmodifiable
    List<? extends HologramPage> getPages();

    /**
     * Get the settings of this hologram.
     *
     * @return The settings.
     * @see HologramSettings
     * @since 2.10.0
     */
    @NotNull
    HologramSettings getSettings();

    /**
     * The position manager of the hologram, used for editing
     * the location of the hologram.
     *
     * @return The position manager of the hologram.
     * @see LocationManager
     * @since 2.10.0
     */
    @NotNull
    LocationManager getLocationManager();

    /**
     * The visibility manager of the hologram, used for managing
     * the visibility of the hologram.
     *
     * @return The visibility manager of the hologram.
     * @see VisibilityManager
     * @since 2.10.0
     */
    @NotNull
    VisibilityManager getVisibilityManager();

}
