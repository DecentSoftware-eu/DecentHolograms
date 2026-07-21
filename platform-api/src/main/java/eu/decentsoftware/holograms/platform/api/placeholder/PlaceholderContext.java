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

package eu.decentsoftware.holograms.platform.api.placeholder;

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;

/**
 * Represents the context in which placeholders are resolved.
 *
 * <p>This interface provides the necessary data and methods to support placeholder
 * replacement operations. It serves as a data container, supplying information such
 * as the player for which placeholders are being resolved.</p>
 *
 * @author d0by
 * @see PlatformPlayer
 * @see PlaceholderProvider
 * @since 2.10.0
 */
public interface PlaceholderContext {

    /**
     * Get the player for which placeholders are being resolved.
     *
     * @return The player.
     * @see PlatformPlayer
     * @since 2.10.0
     */
    PlatformPlayer getPlayer();
}
