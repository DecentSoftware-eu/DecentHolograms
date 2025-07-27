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

package eu.decentsoftware.holograms.skin.mojang;

/**
 * Response from the Mojang API when looking up a player's UUID by their name.
 *
 * @author d0by
 * @see <a href="https://minecraft.wiki/w/Mojang_API#Query_player%27s_UUID">Mojang API Documentation</a>
 * @since 1.0.0
 */
public class MojangUuidResponse {

    /**
     * The UUID of the player.
     */
    private String id;
    /**
     * The name of the player.
     */
    private String name;
    /**
     * The API path used to fetch the UUID.
     * This is provided along with {@link #errorMessage} if the request fails.
     */
    private String path;
    /**
     * Error message if the request fails.
     * This is typically set when a player with the given name cannot be found.
     */
    private String errorMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}