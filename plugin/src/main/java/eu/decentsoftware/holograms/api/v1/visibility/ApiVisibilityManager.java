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

package eu.decentsoftware.holograms.api.v1.visibility;

import eu.decentsoftware.holograms.utils.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public class ApiVisibilityManager implements VisibilityManager {

    private static final String PLAYER_CANNOT_BE_NULL = "player cannot be null";
    // Using WeakHashMap to allow GC to clean up player entries when they disconnect
    private final Map<Player, Visibility> playerVisibilityMap = new WeakHashMap<>();
    private Visibility defaultVisibility = Visibility.VISIBLE;

    @Override
    public void setDefaultVisibility(@NotNull Visibility visibility) {
        Validate.notNull(visibility, "visibility cannot be null");

        this.defaultVisibility = visibility;
    }

    @NotNull
    @Override
    public Visibility getDefaultVisibility() {
        return defaultVisibility;
    }

    @Override
    public void setPlayerVisibility(@NotNull Player player, @Nullable Visibility visibility) {
        Validate.notNull(player, PLAYER_CANNOT_BE_NULL);

        synchronized (playerVisibilityMap) {
            if (visibility == null) {
                playerVisibilityMap.remove(player);
            } else {
                playerVisibilityMap.put(player, visibility);
            }
        }
    }

    @Nullable
    @Override
    public Visibility getPlayerVisibility(@NotNull Player player) {
        Validate.notNull(player, PLAYER_CANNOT_BE_NULL);

        return playerVisibilityMap.get(player);
    }

    @Override
    public boolean isVisibleTo(@NotNull Player player) {
        Validate.notNull(player, PLAYER_CANNOT_BE_NULL);

        Visibility playerVisibility = getPlayerVisibility(player);
        return (playerVisibility == null && getDefaultVisibility() == Visibility.VISIBLE) || playerVisibility == Visibility.VISIBLE;
    }
}
