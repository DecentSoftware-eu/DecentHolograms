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

package eu.decentsoftware.holograms.display;

import eu.decentsoftware.holograms.display.rendering.DisplayRenderingService;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayService {

    private final DisplayRenderingService renderingService;
    private final Map<String, DisplayBase> displays = new ConcurrentHashMap<>();

    public DisplayService(DisplayRenderingService renderingService) {
        this.renderingService = renderingService;
    }

    public void shutdown() {
        this.displays.values().forEach(renderingService::hideForEveryone);
        this.displays.clear();
    }

    public void reload() {
        // TODO: reload displays
    }

    public DisplayBase getDisplay(String name) {
        return displays.get(name);
    }

    public void registerDisplay(DisplayBase display) {
        displays.putIfAbsent(display.getName(), display);
    }

    public void saveDisplay(DisplayBase display) {
        registerDisplay(display);
        // TODO: Save display to file
    }

    public boolean deleteDisplay(String name) {
        DisplayBase removedDisplay = displays.remove(name);
        renderingService.hideForEveryone(removedDisplay);
        return removedDisplay != null;
    }

    public void updateDisplayVisibility(DisplayBase display) {
        renderingService.updateVisibility(display);
    }

    public void updateDisplayContent(DisplayBase displayBase) {
        renderingService.updateContent(displayBase);
    }

    public void updateDisplayProperties(DisplayBase displayBase) {
        renderingService.updateProperties(displayBase);
    }

    public void updateDisplayLocation(DisplayBase displayBase) {
        renderingService.updateDisplayLocation(displayBase);
    }

    public void hideDisplaysForPlayer(Player player) {
        displays.values().forEach(display -> renderingService.hideDisplayForPlayer(display, player));
    }

    public void updateVisibilityForPlayer(Player player) {
        displays.values().forEach(display -> renderingService.updateVisibility(display, player));
    }

    public Set<String> getRegisteredDisplayNames() {
        return displays.keySet();
    }

    public Collection<DisplayBase> getRegisteredDisplays() {
        return displays.values();
    }
}
