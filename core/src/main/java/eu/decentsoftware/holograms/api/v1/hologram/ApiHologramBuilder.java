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
import eu.decentsoftware.holograms.api.v1.platform.DecentPlayer;
import eu.decentsoftware.holograms.api.v1.visibility.ApiVisibilityManager;
import eu.decentsoftware.holograms.api.v1.visibility.Visibility;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ApiHologramBuilder implements HologramBuilder {

    private final List<ApiHologramPageBuilder> pageBuilders = new ArrayList<>();
    private final ApiHologramSettings settings = new ApiHologramSettings();
    private final Consumer<ApiHologram> createdHologramConsumer;
    private DecentLocation location;

    public ApiHologramBuilder(Consumer<ApiHologram> createdHologramConsumer) {
        this.createdHologramConsumer = createdHologramConsumer;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withLocation(@NotNull DecentLocation location) {
        Validate.notNull(location, "location cannot be null");
        this.location = location;
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withInteractive(boolean interactive) {
        settings.setInteractive(interactive);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withDownOrigin(boolean downOrigin) {
        settings.setDownOrigin(downOrigin);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withViewDistance(int viewDistance) {
        Validate.isTrue(viewDistance > 0 && viewDistance <= 48, "viewDistance must be between 1 and 48 blocks");
        settings.setViewDistance(viewDistance);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withUpdateDistance(int updateDistance) {
        Validate.isTrue(updateDistance > 0 && updateDistance <= 48, "updateDistance must be between 1 and 48 blocks");
        settings.setUpdateDistance(updateDistance);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withUpdateInterval(int updateInterval) {
        Validate.isTrue(updateInterval > 0 && updateInterval <= 1200, "updateInterval must be between 1 and 1200 ticks");
        settings.setUpdateInterval(updateInterval);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withUpdating(boolean updating) {
        settings.setUpdating(updating);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withFacing(float facing) {
        Validate.isTrue(facing >= 0.0f && facing <= 360.0f, "facing must be between 0 and 360 degrees");
        settings.setFacing(facing);
        return this;
    }

    @NotNull
    @Contract("_ -> this")
    @Override
    public ApiHologramBuilder withDefaultVisibility(@NotNull Visibility visibility) {
        Validate.notNull(visibility, "visibility cannot be null");
        // TODO
        return this;
    }

    @NotNull
    @Contract("_,_ -> this")
    @Override
    public ApiHologramBuilder withPlayerVisibility(@NotNull DecentPlayer player, @NotNull Visibility visibility) {
        Validate.notNull(player, "player cannot be null");
        Validate.notNull(visibility, "visibility cannot be null");
        // TODO
        return this;
    }

    @NotNull
    @Override
    public ApiHologramPageBuilder withPage() {
        ApiHologramPageBuilder pageBuilder = new ApiHologramPageBuilder(this);
        pageBuilders.add(pageBuilder);
        return pageBuilder;
    }

    @NotNull
    @Override
    public ApiHologram build() {
        Validate.notNull(location, "Cannot build a hologram without a location");
        Validate.isTrue(!pageBuilders.isEmpty(), "Cannot build a hologram with no pages");

        ApiLocationManager locationManager = new ApiLocationManager(location);
        ApiVisibilityManager visibilityManager = new ApiVisibilityManager();
        ApiHologram hologram = new ApiHologram(locationManager, visibilityManager, settings);
        for (ApiHologramPageBuilder pageBuilder : pageBuilders) {
            ApiHologramPage page = pageBuilder.build(hologram);
            hologram.addPage(page);
        }
        createdHologramConsumer.accept(hologram);
        return hologram;
    }
}
