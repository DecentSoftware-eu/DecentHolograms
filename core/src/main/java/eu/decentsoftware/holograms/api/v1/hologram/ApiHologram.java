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

import eu.decentsoftware.holograms.api.v1.location.ApiLocationManager;
import eu.decentsoftware.holograms.api.v1.visibility.ApiVisibilityManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
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

    public void destroy() {
        if (isDestroyed()) {
            return;
        }

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

    @NotNull
    @Override
    public ApiHologramPage getPage(int index) {
        checkDestroyed();
        return pages.get(index);
    }

    public void addPage(@NotNull ApiHologramPage page) {
        checkDestroyed();
        pages.add(page);
    }

    @NotNull
    public ApiHologramPage appendPage() {
        checkDestroyed();
        ApiHologramPage page = createPage();
        pages.add(page);
        return page;
    }

    @NotNull
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

    public void removePage(int index) {
        checkDestroyed();
        pages.remove(index);
    }

    @NotNull
    @Unmodifiable
    @Override
    public List<ApiHologramPage> getPages() {
        checkDestroyed();
        return Collections.unmodifiableList(pages);
    }

    public void clearPages() {
        checkDestroyed();
        pages.clear();
    }

    @NotNull
    @Override
    public ApiHologramSettings getSettings() {
        return settings;
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
