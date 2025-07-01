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

package eu.decentsoftware.holograms.api.v1.hologram;

import eu.decentsoftware.holograms.Validate;
import eu.decentsoftware.holograms.api.v1.location.ApiLocationManager;
import eu.decentsoftware.holograms.api.v1.location.DecentLocation;
import eu.decentsoftware.holograms.api.v1.platform.GenericPlayer;
import eu.decentsoftware.holograms.api.v1.visibility.ApiVisibilityManager;
import eu.decentsoftware.holograms.api.v1.visibility.Visibility;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ApiHologramBuilder implements HologramBuilder {

    private final DecentLocation location;
    private final List<ApiHologramPageBuilder> pageBuilders = new ArrayList<>();
    private final ApiHologramSettings settings = new ApiHologramSettings();

    public ApiHologramBuilder(DecentLocation location) {
        this.location = location;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withInteractive(boolean interactive) {
        settings.setInteractive(interactive);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withDownOrigin(boolean downOrigin) {
        settings.setDownOrigin(downOrigin);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withViewDistance(int viewDistance) {
        Validate.isTrue(viewDistance > 0 && viewDistance <= 48, "viewDistance must be between 1 and 48 blocks");
        settings.setViewDistance(viewDistance);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withUpdateDistance(int updateDistance) {
        Validate.isTrue(updateDistance > 0 && updateDistance <= 48, "updateDistance must be between 1 and 48 blocks");
        settings.setUpdateDistance(updateDistance);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withUpdateInterval(int updateInterval) {
        Validate.isTrue(updateInterval > 0 && updateInterval <= 1200, "updateInterval must be between 1 and 1200 ticks");
        settings.setUpdateInterval(updateInterval);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withUpdating(boolean updating) {
        settings.setUpdating(updating);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withFacing(float facing) {
        Validate.isTrue(facing >= 0.0f && facing <= 360.0f, "facing must be between 0 and 360 degrees");
        settings.setFacing(facing);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public HologramBuilder withDefaultVisibility(@NotNull Visibility visibility) {
        Validate.notNull(visibility, "visibility cannot be null");
        // TODO
        return this;
    }

    @NotNull
    @Contract("_,_ -> this")
    @Override
    public HologramBuilder withPlayerVisibility(@NotNull GenericPlayer player, @NotNull Visibility visibility) {
        Validate.notNull(player, "player cannot be null");
        Validate.notNull(visibility, "visibility cannot be null");
        // TODO
        return this;
    }

    @NotNull
    @Override
    public HologramPageBuilder addPage() {
        ApiHologramPageBuilder pageBuilder = new ApiHologramPageBuilder(this);
        pageBuilders.add(pageBuilder);
        return pageBuilder;
    }

    @NotNull
    @Override
    public Hologram build() {
        Validate.isTrue(!pageBuilders.isEmpty(), "Cannot build a hologram with no pages");

        ApiLocationManager locationManager = new ApiLocationManager(location);
        ApiVisibilityManager visibilityManager = new ApiVisibilityManager();
        ApiHologram hologram = new ApiHologram(locationManager, visibilityManager, settings);
        for (ApiHologramPageBuilder pageBuilder : pageBuilders) {
            ApiHologramPage page = pageBuilder.build(hologram);
            hologram.addPage(page);
        }
        return hologram;
    }
}
