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

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This service is responsible for managing the DecentHolograms API.
 *
 * @author d0by
 * @since 2.10.0
 */
public class BukkitDecentHologramsApiService {

    private final JavaPlugin plugin;
    private final BukkitDecentHologramsApiProviderImpl provider;
    private final BukkitDecentHologramsApiListener listener;

    public BukkitDecentHologramsApiService(JavaPlugin plugin,
                                           BukkitDecentHologramsApiProviderImpl provider,
                                           BukkitDecentHologramsApiListener listener) {
        this.plugin = plugin;
        this.provider = provider;
        this.listener = listener;
    }

    public void initialize() {
        BukkitDecentHologramsApiProvider.setImplementation(provider);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void destroy() {
        HandlerList.unregisterAll(listener);
        BukkitDecentHologramsApiProvider.setImplementation(null);
        provider.destroy();
    }
}
