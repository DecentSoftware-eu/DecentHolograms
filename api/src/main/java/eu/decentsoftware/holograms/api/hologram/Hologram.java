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

package eu.decentsoftware.holograms.api.hologram;

import eu.decentsoftware.holograms.api.location.LocationManager;
import eu.decentsoftware.holograms.api.visibility.VisibilityManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
     * Displays the hologram to the players for whom it is visible.
     * This method ensures the visibility of the hologram and renders
     * all components and pages for the intended audience.
     * <p>
     * This method must be called after creating the hologram to make it visible.
     *
     * @since 2.10.0
     */
    void show();

    /**
     * Hides the hologram from view without destroying it.
     * After calling this method, the hologram will no longer be visible to players
     * until the {@link #show()} method is called to make it visible again.
     * <p>
     * The hologram is hidden by default after creation.
     *
     * @since 2.10.0
     */
    void hide();

    /**
     * Get the page at the given index.
     *
     * @param index The index of the page to get.
     * @return The page at the given index or null if the index is out of bounds.
     * @since 2.10.0
     */
    @Nullable
    HologramPage getPage(int index);

    /**
     * Append a new page to this hologram.
     *
     * @return Instance of this hologram.
     * @since 2.10.0
     */
    @NotNull
    HologramPage appendPage();

    /**
     * Insert a page to this hologram at the specified index.
     *
     * @param index The index to insert the page at.
     * @return Instance of this hologram.
     * @since 2.10.0
     */
    @NotNull
    HologramPage insertPage(int index);

    /**
     * Remove a page from this hologram by its index.
     *
     * @param index The index of the page to remove.
     * @since 2.10.0
     */
    void removePage(int index);

    /**
     * Retrieves an unmodifiable list of all pages associated with this hologram.
     *
     * @return An unmodifiable list of {@link HologramPage} objects representing the pages of this hologram.
     * @since 2.10.0
     */
    @NotNull
    @Unmodifiable
    List<HologramPage> getPages();

    /**
     * Remove all pages from this hologram.
     *
     * @since 2.10.0
     */
    void clearPages();

    /**
     * Check if the hologram is interactive. If this hologram is interactive, it will
     * be clickable by players.
     *
     * @return True if the hologram is interactive.
     * @since 2.10.0
     */
    boolean isInteractive();

    /**
     * Set if the hologram is interactive. If this hologram is interactive, it will
     * be clickable by players.
     *
     * @param interactive True if the hologram is interactive.
     * @since 2.10.0
     */
    void setInteractive(boolean interactive);

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
     * Set if the origin of the hologram is down, meaning the hologram will
     * be built from the bottom of the hologram. If true, the location of the
     * hologram will be at the bottom of the hologram.
     *
     * @param downOrigin True if the origin is down, false otherwise.
     * @since 2.10.0
     */
    void setDownOrigin(boolean downOrigin);

    /**
     * Get the view distance of the hologram. This is the range in which the
     * hologram will be visible.
     *
     * @return The view distance of the hologram.
     * @since 2.10.0
     */
    int getViewDistance();

    /**
     * Set the view distance of the hologram. This is the range in which the
     * hologram will be visible.
     *
     * @param viewDistance The view distance of the hologram.
     * @since 2.10.0
     */
    void setViewDistance(int viewDistance);

    /**
     * Get the update distance of the hologram. This is the range in which the
     * hologram will be updated.
     *
     * @return The update distance of the hologram.
     * @since 2.10.0
     */
    int getUpdateDistance();

    /**
     * Set the update distance of the hologram. This is the range in which the
     * hologram will be updated.
     *
     * @param updateDistance The update distance of the hologram.
     * @since 2.10.0
     */
    void setUpdateDistance(int updateDistance);

    /**
     * Get the update interval of the hologram. This is the number of ticks
     * between each update.
     *
     * @return The update interval of the hologram.
     * @since 2.10.0
     */
    int getUpdateInterval();

    /**
     * Set the update interval of the hologram. This is the number of ticks
     * between each update.
     *
     * @param updateInterval The update interval of the hologram.
     * @since 2.10.0
     */
    void setUpdateInterval(int updateInterval);

    /**
     * Get if the hologram is updating.
     *
     * @return True if the hologram is updating, false otherwise.
     * @since 2.10.0
     */
    boolean isUpdating();

    /**
     * Set if the hologram is updating.
     *
     * @param updating True if the hologram is updating, false otherwise.
     * @since 2.10.0
     */
    void setUpdating(boolean updating);

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
     * Set the facing direction of the hologram. This is the angle in degrees
     * that the hologram is facing.
     * <p>
     * Facing only applies to certain line types. It does not apply to text.
     * <p>
     * This represents the default facing. It can be overridden for individual lines.
     *
     * @param facing The facing direction of the hologram.
     * @since 2.10.0
     */
    void setFacing(float facing);

    /**
     * The position manager of the hologram, used for editing
     * the location of the hologram.
     *
     * @return The position manager of the hologram.
     * @see LocationManager
     * @since 2.10.0
     */
    @NotNull
    LocationManager getPositionManager();

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
