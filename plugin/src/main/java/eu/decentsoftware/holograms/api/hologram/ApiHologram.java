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
    private boolean destroyed = false;

    @Contract(pure = true)
    public ApiHologram(ApiLocationManager positionManager, ApiVisibilityManager visibilityManager, ApiHologramSettings settings) {
        this.positionManager = positionManager;
        this.visibilityManager = visibilityManager;
        this.settings = settings;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }

        hide();
        clearPages();

        destroyed = true;
    }

    public void checkDestroyed() {
        if (destroyed) {
            throw new IllegalStateException("This hologram has been destroyed and cannot be used anymore.");
        }
    }

    public boolean isDestroyed() {
        return destroyed;
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
    public ApiHologramPage getPage(int index) {
        checkDestroyed();
        return pages.get(index);
    }

    @NotNull
    @Override
    public ApiHologramPage appendPage() {
        checkDestroyed();
        ApiHologramPage page = createPage();
        pages.add(page);
        return page;
    }

    @NotNull
    @Override
    public ApiHologramPage insertPage(int index) {
        checkDestroyed();
        ApiHologramPage page = createPage();
        pages.add(index, page);
        return page;
    }

    private ApiHologramPage createPage() {
        checkDestroyed();
        return new ApiHologramPage(this);
    }

    @Override
    public void removePage(int index) {
        checkDestroyed();
        pages.remove(index);
    }

    @NotNull
    @Unmodifiable
    @Override
    public List<HologramPage> getPages() {
        checkDestroyed();
        return ImmutableList.copyOf(pages);
    }

    @Override
    public void clearPages() {
        checkDestroyed();
        pages.clear();
    }

    @Override
    public boolean isInteractive() {
        checkDestroyed();
        return settings.isInteractive();
    }

    @Override
    public void setInteractive(boolean interactive) {
        checkDestroyed();
        settings.setInteractive(interactive);
    }

    @Override
    public boolean isDownOrigin() {
        checkDestroyed();
        return settings.isDownOrigin();
    }

    @Override
    public void setDownOrigin(boolean downOrigin) {
        checkDestroyed();
        settings.setDownOrigin(downOrigin);
    }

    @Override
    public int getViewDistance() {
        checkDestroyed();
        return settings.getViewDistance();
    }

    @Override
    public void setViewDistance(int viewDistance) {
        checkDestroyed();
        settings.setViewDistance(viewDistance);
    }

    @Override
    public int getUpdateDistance() {
        checkDestroyed();
        return settings.getUpdateDistance();
    }

    @Override
    public void setUpdateDistance(int updateDistance) {
        checkDestroyed();
        settings.setUpdateDistance(updateDistance);
    }

    @Override
    public int getUpdateInterval() {
        checkDestroyed();
        return settings.getUpdateInterval();
    }

    @Override
    public void setUpdateInterval(int updateInterval) {
        checkDestroyed();
        settings.setUpdateInterval(updateInterval);
    }

    @Override
    public boolean isUpdating() {
        checkDestroyed();
        return settings.isUpdating();
    }

    @Override
    public void setUpdating(boolean updating) {
        checkDestroyed();
        settings.setUpdating(updating);
    }

    @Override
    public float getFacing() {
        checkDestroyed();
        return settings.getFacing();
    }

    @Override
    public void setFacing(float facing) {
        checkDestroyed();
        settings.setFacing(facing);
    }

    @NotNull
    @Override
    public ApiLocationManager getLocationManager() {
        checkDestroyed();
        return positionManager;
    }

    @NotNull
    @Override
    public ApiVisibilityManager getVisibilityManager() {
        checkDestroyed();
        return visibilityManager;
    }

}
