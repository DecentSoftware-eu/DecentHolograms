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

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class DisplayListener implements Listener {

    private final DisplayService service;
    private final PlatformPlayerService playerService;

    DisplayListener(DisplayService service, PlatformPlayerService playerService) {
        this.service = service;
        this.playerService = playerService;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        PlatformPlayer platformPlayer = playerService.getDecentPlayer(event.getPlayer());
        service.updateVisibilityForPlayer(platformPlayer);
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        PlatformPlayer platformPlayer = playerService.getDecentPlayer(event.getPlayer());
        service.hideDisplaysForPlayer(platformPlayer);
    }
}
