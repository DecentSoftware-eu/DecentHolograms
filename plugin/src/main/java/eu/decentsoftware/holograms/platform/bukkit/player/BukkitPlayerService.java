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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BukkitPlayerService implements PlatformPlayerService {

    private final List<PlatformPlayer> onlinePlayers = new CopyOnWriteArrayList<>();
    private final Map<UUID, PlatformPlayer> playerMap = new ConcurrentHashMap<>();

    @Override
    public @NotNull PlatformPlayer getPlayer(@NotNull Object platformPlayer) {
        if (!(platformPlayer instanceof Player)) {
            throw new IllegalArgumentException("Player object must be of type " + Player.class.getName());
        }
        BukkitPlayer bukkitPlayer = (BukkitPlayer) getPlayer(((Player) platformPlayer).getUniqueId());
        if (bukkitPlayer != null) {
            return bukkitPlayer;
        }
        return new BukkitPlayer((Player) platformPlayer);
    }

    @Override
    public @Nullable PlatformPlayer getPlayer(@NotNull UUID uniqueId) {
        return playerMap.get(uniqueId);
    }

    @Override
    public @NotNull Collection<PlatformPlayer> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void registerPlayer(Player player) {
        BukkitPlayer bukkitPlayer = new BukkitPlayer(player);
        onlinePlayers.add(bukkitPlayer);
        playerMap.put(player.getUniqueId(), bukkitPlayer);
    }

    public void unregisterPlayer(Player player) {
        BukkitPlayer bukkitPlayer = new BukkitPlayer(player);
        onlinePlayers.remove(bukkitPlayer);
        playerMap.remove(player.getUniqueId());
    }
}
