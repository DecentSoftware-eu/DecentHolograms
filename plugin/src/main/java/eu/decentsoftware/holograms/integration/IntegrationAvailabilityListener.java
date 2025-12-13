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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * Listener for integration availability events.
 *
 * <p>This listener is responsible for handling Plugin Enable & Disable events
 * and update the integration availability cache accordingly.</p>
 *
 * @author d0by
 * @see IntegrationAvailabilityService
 * @since 2.9.9
 */
class IntegrationAvailabilityListener implements Listener {

    private final IntegrationAvailabilityService service;

    IntegrationAvailabilityListener(IntegrationAvailabilityService service) {
        this.service = service;
    }

    @EventHandler
    void onPluginEnable(PluginEnableEvent event) {
        updateIntegrationAvailability(event.getPlugin(), true);
    }

    @EventHandler
    void onPluginDisable(PluginDisableEvent event) {
        updateIntegrationAvailability(event.getPlugin(), false);
    }

    private void updateIntegrationAvailability(Plugin plugin, boolean available) {
        String pluginName = plugin.getName();
        Integration integration = Integration.getByPluginName(pluginName);
        if (integration != null) {
            service.setIntegrationAvailability(integration, available);
        }
    }
}
