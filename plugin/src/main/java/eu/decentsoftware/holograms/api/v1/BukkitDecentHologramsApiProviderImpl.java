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

package eu.decentsoftware.holograms.api.v1;

import eu.decentsoftware.holograms.Validate;
import eu.decentsoftware.holograms.api.v1.hologram.ApiHologramManager;
import eu.decentsoftware.holograms.api.v1.platform.BukkitPlatformAdapter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitDecentHologramsApiProviderImpl extends BukkitDecentHologramsApiProvider {

    private final Map<Plugin, BukkitDecentHologramsApiImpl> apiMap = new ConcurrentHashMap<>();

    /**
     * Destroys all API instances and clears the internal map.
     * This method should be called when the plugin is disabled to clean up resources.
     */
    public void destroy() {
        apiMap.values().forEach(BukkitDecentHologramsApiImpl::destroy);
        apiMap.clear();
    }

    /**
     * Destroys the API instance associated with the given plugin, if it exists.
     *
     * @param plugin the plugin whose API instance should be destroyed
     */
    void destroyIfExists(@NotNull Plugin plugin) {
        Validate.notNull(plugin, "plugin cannot be null");

        BukkitDecentHologramsApiImpl api = apiMap.remove(plugin);
        if (api != null) {
            api.destroy();
        }
    }

    @Override
    BukkitDecentHologramsApiImpl getApi(@NotNull Plugin plugin) {
        Validate.notNull(plugin, "plugin cannot be null");
        Validate.isTrue(plugin.isEnabled(), "plugin must be enabled");

        return apiMap.computeIfAbsent(plugin, pluginKey -> {
            ApiHologramManager hologramManager = new ApiHologramManager();
            BukkitPlatformAdapter platformAdapter = new BukkitPlatformAdapter();
            return new BukkitDecentHologramsApiImpl(hologramManager, platformAdapter);
        });
    }

}
