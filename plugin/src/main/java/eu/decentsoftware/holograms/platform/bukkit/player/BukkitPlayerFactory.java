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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Creates the platform-agnostic view of a Bukkit player.
 *
 * <p>Which implementation is produced depends on the server software: some operations that are a
 * plain method call on a single-threaded server have to be expressed differently elsewhere - a
 * teleport across regions, for example. The platform decides once, at startup, and everything
 * downstream sees only {@link PlatformPlayer}.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
@FunctionalInterface
public interface BukkitPlayerFactory {

    /**
     * Wraps a Bukkit player.
     *
     * @param player The Bukkit player.
     * @return The platform-agnostic view of them.
     * @since 2.10.2
     */
    @NotNull
    BukkitPlayer create(@NotNull Player player);
}
