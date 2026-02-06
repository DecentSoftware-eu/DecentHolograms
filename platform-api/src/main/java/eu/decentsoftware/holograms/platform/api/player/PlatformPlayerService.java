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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.UUID;

/**
 * This service is responsible for interacting with platform-specific players.
 *
 * @author d0by
 * @see PlatformPlayer
 * @since 2.10.0
 */
public interface PlatformPlayerService {

    /**
     * Get a platform-agnostic wrapper for the specified platform-specific player.
     *
     * @param platformPlayer The platform-specific player.
     * @return The platform-agnostic wrapper.
     * @see PlatformPlayer
     */
    @NotNull
    PlatformPlayer getPlayer(@NotNull Object platformPlayer);

    /**
     * Get a platform-agnostic wrapper for the player with the specified UUID.
     *
     * @param uniqueId The player UUID.
     * @return The platform-agnostic wrapper or null if no player with the specified UUID is online.
     * @see PlatformPlayer
     */
    @Nullable
    PlatformPlayer getPlayer(@NotNull UUID uniqueId);

    /**
     * Get all online players.
     *
     * @return Platform-agnostic wrappers of all online players.
     * @see PlatformPlayer
     */
    @NotNull
    @Unmodifiable
    Collection<PlatformPlayer> getOnlinePlayers();
}
