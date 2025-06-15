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

package eu.decentsoftware.holograms.api.hologram;

import eu.decentsoftware.holograms.api.location.DecentLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ApiHologramManager implements HologramManager {

    private final Set<ApiHologram> holograms = ConcurrentHashMap.newKeySet();
    private final ApiHologramFactory hologramFactory;

    public ApiHologramManager(ApiHologramFactory hologramFactory) {
        this.hologramFactory = hologramFactory;
    }

    public void destroy() {
        holograms.forEach(ApiHologram::destroy);
        holograms.clear();
    }

    @NotNull
    @Override
    public ApiHologram createHologram(@NotNull DecentLocation location) {
        ApiHologram hologram = hologramFactory.createHologram(location);
        holograms.add(hologram);
        return hologram;
    }

    @NotNull
    @Unmodifiable
    @Override
    public Collection<Hologram> getHolograms() {
        return Collections.unmodifiableCollection(holograms);
    }
}
