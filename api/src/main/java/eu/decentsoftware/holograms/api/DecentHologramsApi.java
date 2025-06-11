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

package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.hologram.HologramManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * This is the main class of the API. It serves as the access point to the API.
 *
 * @author d0by
 * @since 2.10.0
 */
public interface DecentHologramsApi {

    /**
     * Get an instance of the DecentHolograms API for the given plugin.
     *
     * @param plugin The plugin to get the API for (instance of your plugin).
     * @return The instance of the API.
     * @since 2.10.0
     */
    @NotNull
    static DecentHologramsApi getInstance(@NotNull Plugin plugin) {
        return DecentHologramsApiProvider.getImplementation(plugin).getApi(plugin);
    }

    /**
     * Provides access to the {@link HologramManager}, which allows for the creation and management
     * of holograms tied to the API instance.
     *
     * @return An instance of the {@link HologramManager} for the current API instance
     * @see HologramManager
     * @since 2.10.0
     */
    @NotNull
    HologramManager getHologramManager();

}
