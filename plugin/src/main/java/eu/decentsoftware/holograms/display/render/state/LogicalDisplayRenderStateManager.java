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

package eu.decentsoftware.holograms.display.render.state;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LogicalDisplayRenderStateManager {

    private final Map<UUID, Map<String, LogicalDisplayRenderState>> states = new ConcurrentHashMap<>();

    @Nullable
    public LogicalDisplayRenderState getCurrentState(String displayName, UUID playerUniqueId) {
        Map<String, LogicalDisplayRenderState> playerStates = states.get(playerUniqueId);
        if (playerStates == null) {
            return null;
        }
        return playerStates.get(displayName);
    }

    public void updateState(String displayName, UUID playerUniqueId, LogicalDisplayRenderState state) {
        Map<String, LogicalDisplayRenderState> playerStates = states.computeIfAbsent(playerUniqueId, uuid -> new ConcurrentHashMap<>());
        if (state == null) {
            playerStates.remove(displayName);
            if (playerStates.isEmpty()) {
                states.remove(playerUniqueId);
            }
        } else {
            playerStates.put(displayName, state);
        }
    }
}
