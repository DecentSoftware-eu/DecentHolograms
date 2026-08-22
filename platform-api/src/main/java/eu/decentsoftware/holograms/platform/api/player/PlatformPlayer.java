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

package eu.decentsoftware.holograms.platform.api.player;

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a platform-agnostic player.
 *
 * <p>This interface is designed to provide access to basic player data or actions.
 * Implementations are responsible for interacting with the platform-specific player implementation.</p>
 *
 * @author d0by
 * @see PlatformPlayerService
 * @since 2.10.0
 */
public interface PlatformPlayer {

    /**
     * Get the name of the player.
     *
     * @return The name.
     * @since 2.10.0
     */
    @NotNull
    String getName();

    /**
     * Get the unique identifier of the player.
     *
     * @return The unique identifier.
     * @since 2.10.0
     */
    @NotNull
    UUID getUniqueId();

    /**
     * Get the location of the player.
     *
     * @return The location.
     * @see DecentLocation
     * @since 2.10.0
     */
    DecentLocation getLocation();

    /**
     * Check whether the player has the given permission.
     *
     * @param permission The permission to check.
     * @return {@code true} if the player has the permission, {@code false} otherwise.
     * @since 2.10.0
     */
    boolean hasPermission(String permission);

    /**
     * Moves the player to the given location.
     *
     * <p>Returns a future because not every platform can teleport synchronously: on a
     * region-threaded server the player may be owned by another thread, and the move only
     * completes once that thread has run it. Callers that do not care may ignore the result.</p>
     *
     * @param location Where to move the player to.
     * @return A future completing with true if the player was moved, false if the move was
     * rejected - for example because the target world is not loaded.
     * @since 2.10.2
     */
    @NotNull
    CompletableFuture<Boolean> teleport(@NotNull DecentLocation location);

    /**
     * Makes the player say something, as though they had typed it.
     *
     * <p>Text starting with the platform's command prefix is executed as a command by the player,
     * with their permissions.</p>
     *
     * @param message The message to say.
     * @since 2.10.2
     */
    void chat(@NotNull String message);

    /**
     * Sends a message to the player.
     *
     * @param message The message, already formatted for display.
     * @since 2.10.2
     */
    void sendMessage(@NotNull String message);

    /**
     * Plays a sound at the player's position, audible only to them.
     *
     * <p>The canonical form is the fully qualified Mojang key, {@code minecraft:entity.player.levelup}.
     * Deliberately not a platform's own naming - a Bukkit enum constant means nothing elsewhere.</p>
     *
     * @param sound  The Mojang sound key.
     * @param volume The volume.
     * @param pitch  The pitch.
     * @throws IllegalArgumentException If the sound is not recognized, or is not available on the running version.
     * @since 2.10.2
     */
    void playSound(@NotNull String sound, float volume, float pitch);
}
