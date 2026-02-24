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

import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MutableRenderStateManager {

    private final Map<UUID, Map<String, MutableRenderState>> states = new ConcurrentHashMap<>();

    public MutableRenderState getState(UUID playerUniqueId, RenderObjectHandle handle) {
        Map<String, MutableRenderState> stateMap = states.get(playerUniqueId);
        if (stateMap == null) {
            return null;
        }
        return stateMap.get(handle.getId());
    }

    public void setState(UUID playerUniqueId, RenderObjectHandle handle, MutableRenderState state) {
        Map<String, MutableRenderState> playerStates = states.computeIfAbsent(playerUniqueId, uuid -> new ConcurrentHashMap<>());
        if (state == null) {
            playerStates.remove(handle.getId());
            if (playerStates.isEmpty()) {
                states.remove(playerUniqueId);
            }
        } else {
            playerStates.put(handle.getId(), state);
        }
    }
}
