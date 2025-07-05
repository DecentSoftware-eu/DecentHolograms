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

import eu.decentsoftware.holograms.api.v1.visibility.Visibility;
import org.jetbrains.annotations.NotNull;

/**
 * This class holds the settings of a hologram.
 *
 * <p>These settings are used to configure how the hologram behaves and is displayed.</p>
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramSettings {

    /**
     * Check if the hologram is interactive. If this hologram is interactive, it will
     * be clickable by players.
     *
     * @return True if the hologram is interactive.
     * @since 2.10.0
     */
    boolean isInteractive();

    /**
     * Get if the origin of the hologram is down, meaning the hologram will
     * be built from the bottom of the hologram. If true, the location of the
     * hologram will be at the bottom of the hologram.
     *
     * @return True if the origin is down, false otherwise.
     * @since 2.10.0
     */
    boolean isDownOrigin();

    /**
     * Get the view distance of the hologram. This is the range in which the
     * hologram will be visible.
     *
     * @return The view distance of the hologram.
     * @since 2.10.0
     */
    int getViewDistance();

    /**
     * Get the update distance of the hologram. This is the range in which the
     * hologram will be updated.
     *
     * @return The update distance of the hologram.
     * @since 2.10.0
     */
    int getUpdateDistance();

    /**
     * Get the update interval of the hologram. This is the number of ticks
     * between each update.
     *
     * @return The update interval of the hologram.
     * @since 2.10.0
     */
    int getUpdateInterval();

    /**
     * Get if the hologram is updating.
     *
     * @return True if the hologram is updating, false otherwise.
     * @since 2.10.0
     */
    boolean isUpdating();

    /**
     * Get the facing direction of the hologram. This is the angle in degrees
     * that the hologram is facing.
     * <p>
     * Facing only applies to certain line types. It does not apply to text.
     * <p>
     * This represents the default facing. It can be overridden for individual lines.
     *
     * @return The facing direction of the hologram.
     * @since 2.10.0
     */
    float getFacing();

    /**
     * Retrieves the default visibility setting for the hologram.
     * The default visibility determines whether the hologram is visible
     * or hidden by default for players.
     *
     * <p>Default value is {@link Visibility#VISIBLE}.</p>
     *
     * @return The default visibility of the hologram. This value is never null.
     * @since 2.10.0
     */
    @NotNull
    Visibility getDefaultVisibility();

}
