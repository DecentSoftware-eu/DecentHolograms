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

package eu.decentsoftware.holograms.platform.api;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a listener for events triggered by the core.
 *
 * <p>The platform implementation can choose to react to any of these events.</p>
 *
 * @author d0by
 * @since 2.10.0
 */
public interface PlatformEventListener {

    /**
     * Handles the event where a display has been destroyed.
     *
     * <p>By default, this method performs no operation and can be overridden by platform-specific
     * implementations to provide custom behavior when a display is removed. For example,
     * the platform could remove the destroyed display from an internal cache.</p>
     *
     * @param name The name of the display being destroyed. Not null.
     * @since 2.10.0
     */
    default void onDisplayDestroyed(@NotNull String name) {
        // No-op by default
    }
}
