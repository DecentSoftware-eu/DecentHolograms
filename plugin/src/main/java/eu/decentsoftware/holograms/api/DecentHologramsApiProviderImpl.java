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

import eu.decentsoftware.holograms.api.hologram.ApiHologramService;
import eu.decentsoftware.holograms.utils.Validate;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class DecentHologramsApiProviderImpl extends DecentHologramsApiProvider {

    private final Map<Plugin, DecentHologramsApiImpl> apiMap = new ConcurrentHashMap<>();

    void destroy() {
        apiMap.values().forEach(DecentHologramsApiImpl::destroy);
        apiMap.clear();
    }

    @Override
    DecentHologramsApiImpl getApi(@NotNull Plugin plugin) {
        Validate.notNull(plugin, "plugin cannot be null");

        ApiHologramService hologramService = new ApiHologramService();
        DecentHologramsApiImpl apiInstance = new DecentHologramsApiImpl(hologramService);
        apiMap.put(plugin, apiInstance);
        return apiInstance;
    }

}
