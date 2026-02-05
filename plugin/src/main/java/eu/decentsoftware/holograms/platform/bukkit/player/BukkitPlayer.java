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

package eu.decentsoftware.holograms.platform.bukkit.player;

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BukkitPlayer implements PlatformPlayer {

    private final Player platformPlayer;

    public BukkitPlayer(Player platformPlayer) {
        this.platformPlayer = platformPlayer;
    }

    public Player getBukkitPlayer() {
        return platformPlayer;
    }

    @Override
    public @NotNull String getName() {
        return platformPlayer.getName();
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return platformPlayer.getUniqueId();
    }

    @Override
    public DecentLocation getLocation() {
        Location location = platformPlayer.getLocation();
        return new DecentLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }
}
