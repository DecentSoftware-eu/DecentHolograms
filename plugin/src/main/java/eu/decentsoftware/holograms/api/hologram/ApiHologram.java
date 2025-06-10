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

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.api.location.ApiLocationManager;
import eu.decentsoftware.holograms.api.location.LocationManager;
import eu.decentsoftware.holograms.api.visibility.ApiVisibilityManager;
import eu.decentsoftware.holograms.api.visibility.VisibilityManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class ApiHologram implements Hologram {

    private final ApiLocationManager positionManager;
    private final ApiVisibilityManager visibilityManager;
    private final ApiHologramSettings settings;
    private final List<ApiHologramPage> pages = new ArrayList<>();

    @Contract(pure = true)
    public ApiHologram(ApiLocationManager positionManager, ApiVisibilityManager visibilityManager, ApiHologramSettings settings) {
        this.positionManager = positionManager;
        this.visibilityManager = visibilityManager;
        this.settings = settings;
    }

    public void destroy() {
        hide();
        clearPages();
    }

    @Override
    public void show() {
        // TODO
    }

    @Override
    public void hide() {
        // TODO
    }

    @Nullable
    @Override
    public HologramPage getPage(int index) {
        return pages.get(index);
    }

    @NotNull
    @Override
    public HologramPage appendPage() {
        ApiHologramPage page = new ApiHologramPage();
        pages.add(page);
        return page;
    }

    @NotNull
    @Override
    public HologramPage insertPage(int index) {
        ApiHologramPage page = new ApiHologramPage();
        pages.add(index, page);
        return page;
    }

    @Override
    public void removePage(int index) {
        pages.remove(index);
    }

    @NotNull
    @Unmodifiable
    @Override
    public List<HologramPage> getPages() {
        return ImmutableList.copyOf(pages);
    }

    @Override
    public void clearPages() {
        pages.clear();
    }

    @Override
    public boolean isInteractive() {
        return settings.isInteractive();
    }

    @Override
    public void setInteractive(boolean interactive) {
        settings.setInteractive(interactive);
    }

    @Override
    public boolean isDownOrigin() {
        return settings.isDownOrigin();
    }

    @Override
    public void setDownOrigin(boolean downOrigin) {
        settings.setDownOrigin(downOrigin);
    }

    @Override
    public int getViewDistance() {
        return settings.getViewDistance();
    }

    @Override
    public void setViewDistance(int viewDistance) {
        settings.setViewDistance(viewDistance);
    }

    @Override
    public int getUpdateDistance() {
        return settings.getUpdateDistance();
    }

    @Override
    public void setUpdateDistance(int updateDistance) {
        settings.setUpdateDistance(updateDistance);
    }

    @Override
    public int getUpdateInterval() {
        return settings.getUpdateInterval();
    }

    @Override
    public void setUpdateInterval(int updateInterval) {
        settings.setUpdateInterval(updateInterval);
    }

    @Override
    public boolean isUpdating() {
        return settings.isUpdating();
    }

    @Override
    public void setUpdating(boolean updating) {
        settings.setUpdating(updating);
    }

    @Override
    public float getFacing() {
        return settings.getFacing();
    }

    @Override
    public void setFacing(float facing) {
        settings.setFacing(facing);
    }

    @NotNull
    @Override
    public LocationManager getPositionManager() {
        return positionManager;
    }

    @NotNull
    @Override
    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }

}
