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

import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TextDisplayPlayerPageManager {

    private final Map<DisplayViewKey, Integer> pageMap = new ConcurrentHashMap<>();

    public void shutdown() {
        pageMap.clear();
    }

    public Integer getPage(String displayName, UUID playerUniqueId) {
        DisplayViewKey key = DisplayViewKey.of(displayName, playerUniqueId);
        return pageMap.getOrDefault(key, 0);
    }

    public void setPage(String displayName, UUID playerUniqueId, int page) {
        DisplayViewKey key = DisplayViewKey.of(displayName, playerUniqueId);
        pageMap.put(key, page);
    }

    public void clearPage(String displayName, UUID playerUniqueId) {
        DisplayViewKey key = DisplayViewKey.of(displayName, playerUniqueId);
        pageMap.remove(key);
    }

    private static class DisplayViewKey {
        private final String displayName;
        private final UUID playerUniqueId;

        private DisplayViewKey(String displayName, UUID playerUniqueId) {
            this.displayName = displayName;
            this.playerUniqueId = playerUniqueId;
        }

        public static DisplayViewKey of(String displayName, UUID playerUniqueId) {
            Preconditions.checkNotNull(displayName, "displayName cannot be null");
            Preconditions.checkNotNull(playerUniqueId, "playerUniqueId cannot be null");
            return new DisplayViewKey(displayName, playerUniqueId);
        }

        @Override
        public String toString() {
            return "DisplayViewKey{" +
                    "displayName='" + displayName + '\'' +
                    ", playerUniqueId=" + playerUniqueId +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof DisplayViewKey)) {
                return false;
            }
            DisplayViewKey that = (DisplayViewKey) o;
            return Objects.equals(displayName, that.displayName) && Objects.equals(playerUniqueId, that.playerUniqueId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(displayName, playerUniqueId);
        }
    }
}
