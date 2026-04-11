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

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service that keeps track of the availability of integrations.
 *
 * @author d0by
 * @since 2.9.9
 */
public class IntegrationAvailabilityService {

    private final Map<Integration, AtomicBoolean> integrationsAvailability = new EnumMap<>(Integration.class);
    private final JavaPlugin plugin;
    private final PluginManager pluginManager;
    private final IntegrationAvailabilityListener integrationAvailabilityListener;
    private boolean initialized = false;

    /**
     * Create a new instance of {@link IntegrationAvailabilityService}.
     *
     * @param pluginManager The server's plugin manager.
     * @since 2.9.9
     */
    public IntegrationAvailabilityService(JavaPlugin plugin, PluginManager pluginManager) {
        Preconditions.checkNotNull(plugin, "plugin cannot be null");
        Preconditions.checkNotNull(pluginManager, "pluginManager cannot be null");
        this.plugin = plugin;
        this.pluginManager = pluginManager;
        this.integrationAvailabilityListener = new IntegrationAvailabilityListener(this);
    }

    /**
     * Initializes the availability of integrations on startup.
     *
     * <p>This method must be called from the Main thread.</p>
     *
     * @throws IllegalStateException If this method is called from a non-Main thread.
     * @throws IllegalStateException If this service has already been initialized.
     * @since 2.9.9
     */
    public void initialize() {
        if (!Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("This method must be called from the Main thread.");
        }
        if (initialized) {
            throw new IllegalStateException("IntegrationAvailabilityService has already been initialized.");
        }

        initialized = true;
        populateInitialAvailabilities();
        pluginManager.registerEvents(integrationAvailabilityListener, plugin);
    }

    private void populateInitialAvailabilities() {
        for (Integration integration : Integration.values()) {
            boolean available = pluginManager.isPluginEnabled(integration.getPluginName());
            integrationsAvailability.put(integration, new AtomicBoolean(available));
        }
    }

    /**
     * Shuts down the service.
     *
     * <p>This method should only be called when the plugin is being disabled.</p>
     *
     * @since 2.9.9
     */
    public void shutdown() {
        HandlerList.unregisterAll(integrationAvailabilityListener);
    }

    /**
     * Checks if an integration is available.
     *
     * <p>This method can be called asynchronously.</p>
     *
     * @param integration The integration to check.
     * @return True if the integration is available, false otherwise.
     * @see Integration
     * @since 2.9.9
     */
    public boolean isIntegrationAvailable(Integration integration) {
        Preconditions.checkNotNull(integration, "integration cannot be null");
        checkInitialized();

        AtomicBoolean availabilityStatus = integrationsAvailability.get(integration);
        return availabilityStatus != null && availabilityStatus.get();
    }

    /**
     * Sets the availability of an integration.
     *
     * @param integration The integration.
     * @param available   True if the integration is available, false otherwise.
     * @since 2.9.9
     */
    void setIntegrationAvailability(Integration integration, boolean available) {
        Preconditions.checkNotNull(integration, "integration cannot be null");
        checkInitialized();

        AtomicBoolean availabilityStatus = integrationsAvailability.get(integration);
        if (availabilityStatus != null) {
            availabilityStatus.set(available);
        }
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("IntegrationAvailabilityService has not been initialized.");
        }
    }
}
