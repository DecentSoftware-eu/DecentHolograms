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

package eu.decentsoftware.holograms.skin;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a source for player skin textures.
 *
 * @author d0by
 * @since 2.9.6
 */
public interface SkinSource {

    /**
     * Fetches the skin texture for a player by their name.
     *
     * @param playerName The name of the player whose skin texture is to be fetched.
     * @return The skin texture as a base64 encoded string.
     * @throws SkinSourceException If an error occurs while fetching the skin texture.
     */
    @NotNull
    String fetchSkinTextureByPlayerName(@NotNull String playerName);

}
