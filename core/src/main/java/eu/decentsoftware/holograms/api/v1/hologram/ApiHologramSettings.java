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

import eu.decentsoftware.holograms.api.v1.visibility.Visibility;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ApiHologramSettings implements HologramSettings {

    private boolean interactive;
    private boolean downOrigin;
    private int viewDistance;
    private int updateDistance;
    private int updateInterval;
    private boolean updating;
    private float facing;
    private Visibility defaultVisibility;

    @Contract(pure = true)
    public ApiHologramSettings() {
        this.interactive = true;
        this.downOrigin = false;
        this.viewDistance = 48;
        this.updateDistance = 48;
        this.updateInterval = 20;
        this.updating = true;
        this.facing = 0.0f;
        this.defaultVisibility = Visibility.VISIBLE;
    }

    @Override
    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    @Override
    public boolean isDownOrigin() {
        return downOrigin;
    }

    public void setDownOrigin(boolean downOrigin) {
        this.downOrigin = downOrigin;
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    @Override
    public int getUpdateDistance() {
        return updateDistance;
    }

    public void setUpdateDistance(int updateDistance) {
        this.updateDistance = updateDistance;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    @Override
    public boolean isUpdating() {
        return updating;
    }

    public void setUpdating(boolean updating) {
        this.updating = updating;
    }

    @Override
    public float getFacing() {
        return facing;
    }

    public void setFacing(float facing) {
        this.facing = facing;
    }

    @NotNull
    @Override
    public Visibility getDefaultVisibility() {
        return defaultVisibility;
    }

    public void setDefaultVisibility(@NotNull Visibility defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }
}
