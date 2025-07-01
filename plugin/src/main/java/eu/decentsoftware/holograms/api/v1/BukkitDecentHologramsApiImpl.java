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

import eu.decentsoftware.holograms.api.v1.hologram.ApiHologramManager;
import eu.decentsoftware.holograms.api.v1.platform.BukkitPlatformAdapter;
import eu.decentsoftware.holograms.Validate;
import org.jetbrains.annotations.NotNull;

public class BukkitDecentHologramsApiImpl implements BukkitDecentHologramsApi {

    private final ApiHologramManager hologramManager;
    private final BukkitPlatformAdapter platformAdapter;

    public BukkitDecentHologramsApiImpl(@NotNull ApiHologramManager hologramManager, @NotNull BukkitPlatformAdapter platformAdapter) {
        Validate.notNull(hologramManager, "hologramManager cannot be null");
        Validate.notNull(platformAdapter, "platformAdapter cannot be null");
        this.hologramManager = hologramManager;
        this.platformAdapter = platformAdapter;
    }

    public void destroy() {
        hologramManager.destroy();
    }

    @NotNull
    @Override
    public ApiHologramManager getHologramManager() {
        return hologramManager;
    }

    @NotNull
    @Override
    public BukkitPlatformAdapter getPlatformAdapter() {
        return platformAdapter;
    }
}
