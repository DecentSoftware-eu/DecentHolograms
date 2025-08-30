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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DisplayVisibilityService {

    private static final int DISPLAY_RANGE = 256;
    private final Map<String, Set<UUID>> viewersMap = new ConcurrentHashMap<>();

    public boolean isShownToPlayer(DisplayBase display, Player player) {
        return getViewers(display).contains(player.getUniqueId());
    }

    public void addViewer(DisplayBase display, Player player) {
        getViewers(display).add(player.getUniqueId());
    }

    public void removeViewer(DisplayBase display, Player player) {
        getViewers(display).remove(player.getUniqueId());
    }

    public boolean shouldBeShownToPlayer(DisplayBase display, Player player) {
        DecentLocation displayLocation = display.getLocation();
        Location playerLocation = player.getLocation();
        if (!displayLocation.isSameWorld(playerLocation)) {
            return false;
        }
        if (displayLocation.distanceSquared(playerLocation) > DISPLAY_RANGE * DISPLAY_RANGE) {
            return false;
        }
        return true;
    }

    private Set<UUID> getViewers(DisplayBase display) {
        return viewersMap.computeIfAbsent(display.getName(), k -> ConcurrentHashMap.newKeySet());
    }

    public Set<Player> getViewersAsPlayers(DisplayBase display) {
        return getViewers(display).stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet());
    }
}
