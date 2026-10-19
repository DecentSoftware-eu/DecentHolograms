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

import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.display.render.DisplayRenderCoordinator;
import eu.decentsoftware.holograms.display.render.DisplayVisibilityService;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayClickService {

    private final DisplayService displayService;
    private final DisplayEntityRegistry entityRegistry;
    private final DisplayVisibilityService visibilityService;
    private final DisplayRenderCoordinator renderCoordinator;
    private final PlatformPlayerService playerService;
    private final Map<UUID, Long> clickCooldowns = new ConcurrentHashMap<>();

    public DisplayClickService(DisplayService displayService,
                               DisplayEntityRegistry entityRegistry,
                               DisplayVisibilityService visibilityService,
                               DisplayRenderCoordinator renderCoordinator,
                               PlatformPlayerService playerService) {
        this.displayService = displayService;
        this.entityRegistry = entityRegistry;
        this.visibilityService = visibilityService;
        this.renderCoordinator = renderCoordinator;
        this.playerService = playerService;
    }

    public boolean onClick(Player player, int entityId, ClickType clickType) {
        UUID uniqueId = player.getUniqueId();
        if (clickCooldowns.containsKey(uniqueId)
                && System.currentTimeMillis() - clickCooldowns.get(uniqueId) < Settings.CLICK_COOLDOWN * 50L) {
            return false;
        }

        String displayName = entityRegistry.getDisplayName(entityId);
        if (displayName == null) {
            return false;
        }

        DisplayBase display = displayService.getDisplay(displayName);
        if (display == null || !display.hasActions()) {
            return false;
        }

        PlatformPlayer platformPlayer = playerService.getPlayer(player);
        if (!visibilityService.shouldBeShownToPlayer(display, platformPlayer)) {
            return false;
        }

        if (!renderCoordinator.isShownToPlayer(display, platformPlayer)) {
            return false;
        }

        if (!isWithinClickRange(display, platformPlayer)) {
            return false;
        }

        display.executeActions(player, clickType);
        clickCooldowns.put(uniqueId, System.currentTimeMillis());
        return true;
    }

    public void onQuit(Player player) {
        clickCooldowns.remove(player.getUniqueId());
    }

    private boolean isWithinClickRange(DisplayBase display, PlatformPlayer player) {
        DecentLocation displayLocation = display.getLocation();
        DecentLocation playerLocation = player.getLocation();
        if (!displayLocation.isSameWorld(playerLocation)) {
            return false;
        }

        double dx = displayLocation.getX() - playerLocation.getX();
        double dz = displayLocation.getZ() - playerLocation.getZ();
        return dx <= 5 && dx >= -5 && dz <= 5 && dz >= -5;
    }
}
