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

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayerService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BukkitPlayerService implements PlatformPlayerService {

    @Override
    public @NotNull PlatformPlayer getPlayer(@NotNull Object platformPlayer) {
        if (!(platformPlayer instanceof Player)) {
            throw new IllegalArgumentException("Player object must be of type Player");
        }
        return new BukkitPlayer((Player) platformPlayer);
    }

    @Override
    public @Nullable PlatformPlayer getPlayer(@NotNull UUID uniqueId) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player == null) {
            return null;
        }
        return new BukkitPlayer(player);
    }

    @Override
    public @NotNull @Unmodifiable Collection<PlatformPlayer> getOnlinePlayers() {
        List<PlatformPlayer> players = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            players.add(getPlayer(onlinePlayer));
        }
        return Collections.unmodifiableCollection(players);
    }
}
