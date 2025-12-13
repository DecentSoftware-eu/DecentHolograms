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

package eu.decentsoftware.holograms.integration;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.jetbrains.annotations.Nullable;

/**
 * Represents external plugin integrations that can be used for additional functionality.
 *
 * @author d0by
 * @since 2.9.9
 */
public enum Integration {
    PLACEHOLDER_API("PlaceholderAPI"),
    HEAD_DATABASE("HeadDatabase");

    private final String pluginName;

    Integration(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * Get the plugin name.
     *
     * @return Plugin name.
     * @since 2.9.9
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Check if integration is available.
     *
     * @return True if integration is available, false otherwise.
     * @see IntegrationAvailabilityService
     * @since 2.9.9
     */
    public boolean isAvailable() {
        IntegrationAvailabilityService integrationAvailabilityService = DecentHologramsAPI.get().getIntegrationAvailabilityService();
        return integrationAvailabilityService.isIntegrationAvailable(this);
    }

    /**
     * Get integration by plugin name.
     *
     * @param pluginName Name of the plugin.
     * @return Integration or null if not found.
     * @since 2.9.9
     */
    @Nullable
    public static Integration getByPluginName(String pluginName) {
        for (Integration integration : values()) {
            if (integration.getPluginName().equalsIgnoreCase(pluginName)) {
                return integration;
            }
        }
        return null;
    }
}
