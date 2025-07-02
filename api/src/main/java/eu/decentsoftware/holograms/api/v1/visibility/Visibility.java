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

package eu.decentsoftware.holograms.api.v1.visibility;

/**
 * Visibility of a hologram for a specific player. This is used to
 * determine if the hologram should be visible for a player.
 *
 * @author d0by
 * @since 2.10.0
 */
public enum Visibility {
    /**
     * The hologram is visible for the player. This is the default value.
     */
    VISIBLE,
    /**
     * The hologram is hidden for the player.
     */
    HIDDEN
}