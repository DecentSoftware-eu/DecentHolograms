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

import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.render.RenderObjectHandle;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LogicalDisplayRenderStateManager {

    private final Map<UUID, Map<String, LogicalDisplayRenderState>> states = new ConcurrentHashMap<>();

    @Nullable
    public LogicalDisplayRenderState getCurrentState(RenderObjectHandle handle, DisplayRenderContext context) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        return getPlayerRenderStateMap(playerUniqueId).get(handle.getId());
    }

    public void updateState(RenderObjectHandle handle, DisplayRenderContext context, LogicalDisplayRenderState state) {
        UUID playerUniqueId = context.getPlayer().getUniqueId();
        getPlayerRenderStateMap(playerUniqueId).put(handle.getId(), state);
    }

    private Map<String, LogicalDisplayRenderState> getPlayerRenderStateMap(UUID playerUniqueId) {
        return states.computeIfAbsent(playerUniqueId, uuid -> new ConcurrentHashMap<>());
    }
}
