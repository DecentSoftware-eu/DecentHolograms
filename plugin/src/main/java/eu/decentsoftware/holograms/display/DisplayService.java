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

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.config.DisplayConfigException;
import eu.decentsoftware.holograms.display.config.DisplayPersistenceService;
import eu.decentsoftware.holograms.display.rendering.DisplayRenderingService;
import eu.decentsoftware.holograms.display.text.TextDisplayViewService;
import eu.decentsoftware.holograms.location.DecentLocation;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DisplayService {

    private final DisplayPersistenceService persistenceService;
    private final DisplayRenderingService renderingService;
    private final TextDisplayViewService viewService;
    private final Map<String, DisplayBase> displays = new ConcurrentHashMap<>();

    public DisplayService(DisplayPersistenceService persistenceService,
                          DisplayRenderingService renderingService,
                          TextDisplayViewService viewService) {
        this.persistenceService = persistenceService;
        this.renderingService = renderingService;
        this.viewService = viewService;
    }

    public void shutdown() {
        this.displays.values().forEach(renderingService::hideForEveryone);
        this.displays.clear();
    }

    public void reload() {
        shutdown();

        try {
            Log.info("Loading displays...");
            List<DisplayBase> loadedDisplays = persistenceService.loadAllDisplays();
            for (DisplayBase display : loadedDisplays) {
                registerDisplay(display);
                renderingService.updateVisibility(display);
            }
            Log.info("Loaded %d displays!", loadedDisplays.size());
        } catch (DisplayConfigException e) {
            Log.error("Failed to load displays", e);
        }
    }

    public DisplayBase getDisplay(String name) {
        return displays.get(name);
    }

    public void registerDisplay(DisplayBase display) {
        displays.putIfAbsent(display.getName(), display);
    }

    public void saveDisplay(DisplayBase display) {
        registerDisplay(display);
        persistenceService.saveDisplay(display);
    }

    public boolean deleteDisplay(String name) {
        DisplayBase removedDisplay = displays.remove(name);
        if (removedDisplay == null) {
            return false;
        }
        renderingService.hideForEveryone(removedDisplay);
        persistenceService.deleteDisplay(removedDisplay);
        return true;
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
        displays.values().forEach(display -> {
            renderingService.hideDisplayForPlayer(display, player);
            viewService.clearView(display.getName(), player.getUniqueId());
        });
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

    public Collection<DisplayBase> getRegisteredDisplaysInRadius(DecentLocation location, double radius) {
        return displays.values().stream()
                .filter(display -> display.getLocation().distanceSquared(location) <= radius * radius)
                .collect(Collectors.toList());
    }
}
