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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayEntityRegistry {

    private final Map<Integer, String> entityIdToDisplay = new ConcurrentHashMap<>();
    private final Map<String, Set<Integer>> displayToEntityIds = new ConcurrentHashMap<>();

    public void registerClickableEntities(String displayName, Collection<Integer> entityIds) {
        unregister(displayName);
        Set<Integer> ids = ConcurrentHashMap.newKeySet();
        for (int entityId : entityIds) {
            entityIdToDisplay.put(entityId, displayName);
            ids.add(entityId);
        }
        if (!ids.isEmpty()) {
            displayToEntityIds.put(displayName, ids);
        }
    }

    public void unregister(String displayName) {
        Set<Integer> entityIds = displayToEntityIds.remove(displayName);
        if (entityIds != null) {
            entityIds.forEach(entityIdToDisplay::remove);
        }
    }

    public String getDisplayName(int entityId) {
        return entityIdToDisplay.get(entityId);
    }
}
