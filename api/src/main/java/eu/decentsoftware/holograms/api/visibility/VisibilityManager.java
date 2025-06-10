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

package eu.decentsoftware.holograms.api.visibility;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is responsible for managing the visibility of a hologram for players.
 * It is able to show/hide the hologram for specific players.
 * <p>
 * To modify the visibility of the hologram for a specific player, use
 * {@link #setPlayerVisibility(Player, Visibility)} or {@link #resetPlayerVisibility(Player)}.
 * To get the visibility of the hologram for a specific player,
 * use {@link #getPlayerVisibility(Player)}.
 * <p>
 * Player visibility is always prioritized over the default visibility.
 * <p>
 * To modify the default visibility of the hologram, use {@link #setDefaultVisibility(Visibility)}.
 * To get the default visibility of the hologram, use {@link #getDefaultVisibility()}.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface VisibilityManager {

    /**
     * Set the default visibility of the hologram. This is the visibility used for
     * players that don't have a custom visibility setting.
     *
     * @param visibility The default visibility of the hologram.
     * @see #setDefaultVisibility(Visibility)
     * @see #isVisibleByDefault()
     * @since 2.10.0
     */
    void setDefaultVisibility(@NotNull Visibility visibility);

    /**
     * Get the default visibility of the hologram. This is the visibility used for
     * players that don't have a custom visibility setting.
     *
     * @return The default visibility of the hologram.
     * @see #setDefaultVisibility(Visibility)
     * @see #isVisibleByDefault()
     * @since 2.10.0
     */
    @NotNull
    Visibility getDefaultVisibility();

    /**
     * Check if the hologram is visible by default. This is the case if the hologram is not
     * restricted to any players and is automatically visible for all players.
     *
     * @return True if the hologram is visible by default, false otherwise.
     * @see #setDefaultVisibility(Visibility)
     * @see #getDefaultVisibility()
     * @since 2.10.0
     */
    default boolean isVisibleByDefault() {
        return getDefaultVisibility() == Visibility.VISIBLE;
    }

    /**
     * Set the visibility of the hologram for the given player. Player visibility
     * is always prioritized over the default visibility, and the hologram will
     * always be visible for the player if their visibility is set to visible.
     *
     * @param player     The player to set the visibility for.
     * @param visibility The visibility to set.
     * @see #getPlayerVisibility(Player)
     * @see #isVisibleByDefault()
     * @since 2.10.0
     */
    void setPlayerVisibility(@NotNull Player player, @Nullable Visibility visibility);

    /**
     * Set the visibility of the hologram for the given player to the default
     * visibility.
     *
     * @param player The player to reset the visibility for.
     * @see #setPlayerVisibility(Player, Visibility)
     * @since 2.10.0
     */
    default void resetPlayerVisibility(@NotNull Player player) {
        setPlayerVisibility(player, null);
    }

    /**
     * Get the visibility of the hologram for the given player. Player visibility
     * is always prioritized over the default visibility, and the hologram will
     * always be visible for the player if their visibility is set to visible.
     *
     * @param player The player to get the visibility for.
     * @return The visibility of the hologram for the given player.
     * @see #setPlayerVisibility(Player, Visibility)
     * @see #isVisibleByDefault()
     * @since 2.10.0
     */
    @Nullable
    Visibility getPlayerVisibility(@NotNull Player player);

    /**
     * Check if the hologram is visible to the given player. This method checks
     * the player's visibility setting and the default visibility of the hologram.
     *
     * @param player The player to check the visibility for.
     * @return True if the hologram is visible to the player, false otherwise.
     * @see #getPlayerVisibility(Player)
     * @see #isVisibleByDefault()
     * @since 2.10.0
     */
    boolean isVisibleTo(@NotNull Player player);

}