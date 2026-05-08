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
}
