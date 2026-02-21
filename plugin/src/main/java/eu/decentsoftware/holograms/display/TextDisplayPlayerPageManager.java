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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TextDisplayPlayerPageManager {

    private final Map<UUID, Map<String, Integer>> pageMap = new HashMap<>();

    public void shutdown() {
        pageMap.clear();
    }

    public int getPage(String displayName, UUID playerUniqueId) {
        Map<String, Integer> playerPageMap = pageMap.get(playerUniqueId);
        if (playerPageMap == null) {
            return 0;
        }
        return playerPageMap.getOrDefault(displayName, 0);
    }

    public void setPage(String displayName, UUID playerUniqueId, int page) {
        Map<String, Integer> playerPageMap = pageMap.computeIfAbsent(playerUniqueId, k -> new ConcurrentHashMap<>());
        playerPageMap.put(displayName, page);
    }

    public void clearPage(String displayName, UUID playerUniqueId) {
        Map<String, Integer> playerPageMap = pageMap.get(playerUniqueId);
        if (playerPageMap != null) {
            playerPageMap.remove(displayName);
        }
    }
}
