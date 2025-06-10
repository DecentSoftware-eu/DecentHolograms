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

package eu.decentsoftware.holograms.api.location;

import eu.decentsoftware.holograms.utils.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ApiLocationManager implements LocationManager {

    private DecentLocation location;
    private LocationBinder locationBinder;
    private DecentOffsets offsets = DecentOffsets.ZERO;

    public ApiLocationManager(@NotNull DecentLocation location) {
        Validate.notNull(location, "location cannot be null");
        this.location = location;
    }

    @NotNull
    @Override
    public DecentLocation getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull DecentLocation location) {
        Validate.notNull(location, "location cannot be null");

        this.location = location;
    }

    @NotNull
    @Override
    public DecentLocation getActualLocation() {
        DecentLocation baseLocation;
        if (locationBinder != null) {
            baseLocation = locationBinder.getLocation();
        } else {
            baseLocation = getLocation();
        }

        return new DecentLocation(
                baseLocation.getWorldName(),
                baseLocation.getX() + offsets.getX(),
                baseLocation.getY() + offsets.getY(),
                baseLocation.getZ() + offsets.getZ()
        );
    }

    @Override
    public void bindLocation(@Nullable LocationBinder locationBinder) {
        this.locationBinder = locationBinder;
    }

    @Nullable
    @Override
    public LocationBinder getLocationBinder() {
        return locationBinder;
    }

    @NotNull
    @Override
    public DecentOffsets getOffsets() {
        return offsets;
    }

    @Override
    public void setOffsets(@NotNull DecentOffsets offsets) {
        Validate.notNull(offsets, "offsets cannot be null");

        this.offsets = offsets;
    }
}
