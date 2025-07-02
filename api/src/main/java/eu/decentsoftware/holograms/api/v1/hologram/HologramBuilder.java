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

import eu.decentsoftware.holograms.api.v1.platform.DecentPlayer;
import eu.decentsoftware.holograms.api.v1.visibility.Visibility;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Builder for creating holograms.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface HologramBuilder {

    /**
     * Sets whether the hologram is interactive.
     *
     * @param interactive Whether the hologram can be interacted with.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withInteractive(boolean interactive);

    /**
     * Sets whether the hologram uses a down origin point.
     *
     * @param downOrigin Whether to use down origin point.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withDownOrigin(boolean downOrigin);

    /**
     * Sets the maximum distance from which the hologram is visible.
     *
     * @param viewDistance The view distance in blocks.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withViewDistance(int viewDistance);

    /**
     * Sets the maximum distance at which the hologram will update.
     *
     * @param updateDistance The update distance in blocks.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withUpdateDistance(int updateDistance);

    /**
     * Sets the interval at which the hologram updates.
     *
     * @param updateInterval The update interval in ticks.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withUpdateInterval(int updateInterval);

    /**
     * Sets whether the hologram should update automatically.
     *
     * @param updating Whether to enable automatic updates.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withUpdating(boolean updating);

    /**
     * Sets the facing direction of the hologram.
     *
     * @param facing The facing angle in degrees.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withFacing(float facing);

    /**
     * Sets the default visibility state for the hologram.
     *
     * @param visibility The default visibility state.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_ -> this")
    HologramBuilder withDefaultVisibility(@NotNull Visibility visibility);

    /**
     * Sets the visibility state for a specific player.
     *
     * @param player     The player to set visibility for.
     * @param visibility The visibility state for the player.
     * @return This builder instance.
     */
    @NotNull
    @Contract("_,_ -> this")
    HologramBuilder withPlayerVisibility(@NotNull DecentPlayer player, @NotNull Visibility visibility);

    /**
     * Adds a new page to the hologram.
     *
     * @return A new hologram page builder.
     */
    @NotNull
    HologramPageBuilder addPage();

    /**
     * Builds and returns the final hologram.
     *
     * @return The constructed hologram.
     */
    @NotNull
    Hologram build();

}
